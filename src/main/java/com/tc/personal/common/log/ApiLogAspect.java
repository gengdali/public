package com.tc.personal.common.log;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tc.personal.common.api.CommonResult;
import com.tc.personal.common.component.MapperRegistryHelper;
import com.tc.personal.common.log.annotation.ApiLog;
import com.tc.personal.modules.ums.mapper.UmsSysLogMapper;
import com.tc.personal.modules.ums.model.UmsSysLog;
import com.tc.personal.security.domain.AdminUserDetails;
import com.tc.personal.utils.ChineseNameUtils;
import com.tc.personal.utils.DateUtil;
import com.tc.personal.utils.HttpContextUtils;
import com.tc.personal.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 日志切面
 *
 * @author 阿鸿
 */
@Aspect
@Component
@Slf4j
public class ApiLogAspect {

    @Lazy
    @Resource
    private UmsSysLogMapper sysLogMapper;

    @Autowired
    private MapperRegistryHelper mapperRegistryHelper;


    /**
     * 此处的切点是注解的方式
     * 只要出现 @LogAnnotation注解都会进入
     */
    @Pointcut("@annotation(com.tc.personal.common.log.annotation.ApiLog)")
    public void logPointCut() {

    }

    /**
     * 环绕增强,相当于MethodInterceptor
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //用户名
        AdminUserDetails userDetails = (AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UmsSysLog sysLog = new UmsSysLog();
        sysLog.setUsername(userDetails.getUsername());
        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        String method = request.getMethod();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method2 = signature.getMethod();
        ApiLog logAnnotation = method2.getAnnotation(ApiLog.class);
        Class<?> clazz = null;
        if (logAnnotation != null) {
            clazz = logAnnotation.value();
            //注解上的描述
            sysLog.setOperation(logAnnotation.title());
            sysLog.setOperationType(logAnnotation.action());
        }
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className.replace("com.tc.assessment.modules.ums.controller.", "") + "." + methodName + "()");
        Map<String, BaseMapper<?>> mapperMap = mapperRegistryHelper.getAllMappers();
        // 获取实体对象的初始状态
        BaseMapper<?> baseMapper = mapperMap.get(clazz.getSimpleName());
        long beginTime = System.currentTimeMillis();
        //请求的参数
        Object[] args = point.getArgs();
        if ("PUT".equals(method)) {
            if (args != null) {
                Object arg = args[0];
                if (arg instanceof Object) {
                    Field field = arg.getClass().getDeclaredField("id");
                    field.setAccessible(true);
                    Long id = (Long) field.get(arg);
                    if (id != null) {
                        Object oldEntity = baseMapper.selectById(id);
                        //执行方法
                        CommonResult result = (CommonResult) point.proceed();
                        //执行时长(毫秒)
                        long time = System.currentTimeMillis() - beginTime;
                        // 获取实体对象的最终状态
                        Object newEntity = baseMapper.selectById(id);
                        // 记录日志
                        if (oldEntity != null && newEntity != null) {
                           /* Map<String, String>  oldEntityFields = EntityCommentUtil.getFieldComments(oldEntity.getClass());
                            Map<String, String>  newEntityFields = EntityCommentUtil.getFieldComments(newEntity.getClass());
                            EntityCommentUtil.replaceEntityFields(oldEntity,oldEntityFields);
                            EntityCommentUtil.replaceEntityFields(newEntity,oldEntityFields);*/
                            String oldOne = ChineseNameUtils.toJsonString(oldEntity);
                            String newOne = ChineseNameUtils.toJsonString(newEntity);
                            String params = "数据 " + clazz.getSimpleName() + " 从  -> " + oldOne + " 修改为 ->" + newOne;
                            log.info("数据 {}  从 {} 修改为 {}", clazz.getSimpleName(), oldOne, newOne);
                            sysLog.setParams(params);
                            sysLog.setPeopleOperationRecord(userDetails.getUsername() + " 编辑 " + oldOne);
                        }
                        //保存日志
                        try {
                            saveSysLog(sysLog, time, result);
                        } catch (Exception e) {
                            log.error("sysLog,exception:{}", e, e);
                        }
                        return result;
                    }
                }
            }
        }
        if ("DELETE".equals(method)) {
            if (args != null) {
                Object arg = args[0];
                String s = arg.toString();
                long id = Long.parseLong(s);
                Object oldEntity = baseMapper.selectById(id);
                //执行方法
                CommonResult result = (CommonResult) point.proceed();
                //执行时长(毫秒)
                long time = System.currentTimeMillis() - beginTime;
                // 获取实体对象的最终状态
                Object newEntity = baseMapper.selectById(id);
                // 记录日志
                if (oldEntity != null && newEntity == null) {
                    /*EntityCommentUtil.replaceEntityFieldComments(oldEntity);*/
                    String oldOne = ChineseNameUtils.toJsonString(oldEntity);
                    log.info("删除了 {} 数据为 {}", clazz.getSimpleName(), oldEntity);
                    String params = "删除->" + oldOne;
                    sysLog.setParams(params);
                    sysLog.setPeopleOperationRecord(userDetails.getUsername() + " 删除 " + oldOne);
                }
                try {
                    saveSysLog(sysLog, time, result);
                } catch (Exception e) {
                    log.error("sysLog,exception:{}", e, e);
                }
                return result;
            }
        }
        if (args != null) {
            try {
                String paramter = "";
                Object[] arguments = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                        continue;
                    }
                    arguments[i] = args[i];
                }
                if (arguments != null) {
                    Object objectParameter = null;
                    try {
                        for (int i = 0; i < arguments.length; i++) {
                            if (arguments[i] instanceof Object) {
                                objectParameter = arguments[i];
                            }
                        }
                        if (objectParameter != null) {
                            paramter = ChineseNameUtils.toJsonString(objectParameter);
                        } else {
                            paramter = Arrays.toString(arguments);
                        }

                    } catch (Exception e) {
                        paramter = arguments.toString();
                    }
                }
                sysLog.setParams(paramter);
                sysLog.setPeopleOperationRecord(userDetails.getUsername() + "->" + paramter);
            } catch (Exception e) {
                log.error("sysLog,exception:{}", e, e);
            }
            //执行方法
            CommonResult result = (CommonResult) point.proceed();
            //执行时长(毫秒)
            long time = System.currentTimeMillis() - beginTime;
            saveSysLog(sysLog, time, result);
            return result;
        }
        return null;
    }

    /**
     * 把日志保存
     */
    private void saveSysLog(ProceedingJoinPoint joinPoint, long time, CommonResult result) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UmsSysLog sysLog = new UmsSysLog();
        ApiLog logAnnotation = method.getAnnotation(ApiLog.class);
        if (logAnnotation != null) {
            //注解上的描述
            sysLog.setOperation(logAnnotation.title());
            sysLog.setOperationType(logAnnotation.action());
        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className.replace("com.tc.assessment.modules.ums.controller.", "") + "." + methodName + "()");
        log.info("请求{}.{}耗时{}毫秒", className, methodName, time);
        try {
            //请求的参数
            Object[] args = joinPoint.getArgs();
            Object[] arguments = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
//ServletRequest不能序列化，从入参里排除，否则报异常：java.lang.IllegalStateException: It is illegal to call this method if the current request is not in asynchronous mode (i.e. isAsyncStarted() returns false)
//ServletResponse不能序列化 从入参里排除，否则报异常：java.lang.IllegalStateException: getOutputStream() has already been called for this response
                    continue;
                }
                arguments[i] = args[i];
            }
            String paramter = "";
            if (arguments != null) {
                try {
                    paramter = Arrays.toString(arguments);
                } catch (Exception e) {
                    paramter = arguments.toString();
                }
            }
            sysLog.setParams(paramter);
        } catch (Exception e) {
            log.error("sysLog,exception:{}", e, e);
        }
        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        log.info("Ip{}，接口地址{}，请求方式{}，入参：{}", sysLog.getIp(), request.getRequestURL(), request.getMethod(), sysLog.getParams());
        //用户名
        AdminUserDetails userDetails = (AdminUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        sysLog.setUsername(userDetails.getUsername());
        sysLog.setUrl(String.valueOf(request.getRequestURL()));
        sysLog.setRequestMethod(request.getMethod());
        String s = DateUtil.DateToString(new Date(), 2);
        sysLog.setCreateTime(s);
        if (result != null) {
            sysLog.setStatus(String.valueOf(result.getCode()).equals("200") ? 1 : 0);
        }
        log.info(sysLog.toString());
        sysLogMapper.insert(sysLog);
    }

    /**
     * 把日志保存
     */
    private void saveSysLog(UmsSysLog sysLog, long time, CommonResult result) {
        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLog.setIp(IPUtils.getIpAddr(request));
        log.info("Ip{}，接口地址{}，请求方式{}，入参：{}", sysLog.getIp(), request.getRequestURL(), request.getMethod(), sysLog.getParams());
        sysLog.setUrl(String.valueOf(request.getRequestURL()));
        sysLog.setRequestMethod(request.getMethod());
        String s = DateUtil.DateToString(new Date(), 2);
        sysLog.setCreateTime(s);
        if (result != null) {
            sysLog.setStatus(String.valueOf(result.getCode()).equals("200") ? 1 : 0);
        }
        log.info(sysLog.toString());
        sysLogMapper.insert(sysLog);
    }

    public String judgeArgs(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest || args[i] instanceof ServletResponse || args[i] instanceof MultipartFile) {
                return "Stream";
            }
            if (args[i] instanceof String) {
                return "String";
            }
            if (args[i] instanceof Long) {
                return "Long";
            }
            if (args[i] instanceof Integer) {
                return "Integer";
            }
        }
        return "";
    }
}

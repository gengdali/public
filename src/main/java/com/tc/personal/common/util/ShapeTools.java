package com.tc.personal.common.util;


import cn.hutool.core.util.CharsetUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tc.personal.common.anotation.Shp;
import com.tc.personal.common.exception.ApiException;
import com.tc.personal.modules.ums.enumeration.ShpCheckEnum;
import lombok.extern.slf4j.Slf4j;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author: wh
 * @create: 2022-10-18 17:50
 * @description shp文件操作工具类
 **/
@Slf4j
public class ShapeTools<T> {
    /**
     * shp文件定义字段时，字段名长度不能多于10个字符
     */
    private final int FILED_LENGTH_LIMIT = 10;
    /**
     * shp文件定义字段时，空间属性字段必须是the_geom
     */
    private final String GEOM_FIELD_NAME = "the_geom";
    private static final int BATCH_COUNT = 1000;
    private static final SecurityManager securityManager = new SecurityManager();
    private BaseMapper<T> baseMapper;
    private Class<?> clazz;
    private ThreadPoolTaskExecutor executor;
    private DataSourceTransactionManager dataSourceTransactionManager;
    private TransactionDefinition transactionDefinition;

    private final List<T> cacheData = new ArrayList<>();

    public ShapeTools() {
    }

    public ShapeTools(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    public ShapeTools(BaseMapper<T> baseMapper, Class<?> clazz) {
        this.baseMapper = baseMapper;
        this.clazz = clazz;
    }

    public ShapeTools(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ShapeTools(BaseMapper<T> baseMapper, ThreadPoolTaskExecutor executor, DataSourceTransactionManager dataSourceTransactionManager, TransactionDefinition transactionDefinition) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
        this.transactionDefinition = transactionDefinition;
        this.baseMapper = baseMapper;
        this.executor = executor;
    }


    /**
     * 读取shp文件，返回对象集合
     *
     * @param file             文件
     * @param shpEntityBuilder feature对象转换成指定对象的接口
     * @return List<T> 结构化的shp数据
     * @throws Exception 异常信息
     */
    // @Transactional(rollbackFor = Exception.class)
    public List<T> readShp(File file, ShpEntityBuilder<T> shpEntityBuilder) throws Exception {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore sds;
        SimpleFeatureIterator iterator;
        sds = (ShapefileDataStore) dataStoreFactory.createDataStore(file.toURI().toURL());
//        sds.setCharset(StandardCharsets.UTF_8);
        sds.setCharset(CharsetUtil.CHARSET_GBK);
        iterator = sds.getFeatureSource().getFeatures().features();
        while (iterator.hasNext()) {
            if (iterator.hasNext()) {
                FeatureJSON fjson = new FeatureJSON();
                StringWriter writer = new StringWriter();
                SimpleFeature next = iterator.next();
                fjson.writeFeature(next, writer);
                //JSONObject json = JSONObject.parseObject(writer.toString());
                T data = shpEntityBuilder.createEntity(next);
                if (null != data) {
                    cacheData.add(data);
                }
            } else {
                break;
            }
        }
        iterator.close();
        sds.dispose();
        return cacheData;
    }

//    private String getMethodName(String field){
//        char first = field.charAt(0);
//        String str = String.valueOf(first).toUpperCase();
//        String name = field.replaceFirst(String.valueOf(first), str);
//        return "get" + name;
//    }

    private String getSetMethodName(String field) {
        char first = field.charAt(0);
        String str = String.valueOf(first).toUpperCase();
        String name = field.replaceFirst(String.valueOf(first), str);
        return "set" + name;
    }

    /**
     * 读取shp文件后反射 映射值
     *
     * @param clazz shp实体类
     */
    public ShpEntityBuilder<T> shpEntityBuilder(Class<?> clazz) {
        return feature -> {
            //获取所有的字段
            Field[] fields = clazz.getDeclaredFields();
            T object = (T) clazz.newInstance();
            for (Field field : fields) {
                //设置允许访问私有变量
                field.setAccessible(true);
                //获取shp中的属性
                Object attribute = feature.getAttribute(field.getName());
                if (!ObjectUtils.isEmpty(attribute)) {
                    field.set(object, attribute);
                    continue;
                }
                if (field.getName().equals("geom")) {
                    String str = feature.getAttribute(GEOM_FIELD_NAME).toString();
                    field.set(object, str);
                    TableId annotation = field.getAnnotation(TableId.class);
                }
                /*if (field.getName().equals("gid")) {
                    field.set(object, UUID.fastUUID());
                }*/
            }
            return object;
        };
    }


    public static Boolean checkShp(File file) throws Exception {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore sds = null;
        SimpleFeatureIterator iterator = null;
        try {
            sds = (ShapefileDataStore) dataStoreFactory.createDataStore(file.toURI().toURL());
            sds.setCharset(Charset.forName("GBK"));
            iterator = sds.getFeatureSource().getFeatures().features();
            FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = sds.getFeatureSource();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                //获取type
                Object type = feature.getAttribute("type");
                if (type == null) {
                    log.error("文件type属性不存在");
                    throw new ApiException("type属性不存在");
                }
                Class desc = ShpCheckEnum.getDesc(type.toString());
                Field[] declaredFields = desc.getDeclaredFields();
                //获取所有字段名称
                Map<String, String> map = new HashMap<>();
                Arrays.stream(declaredFields).forEach(t -> {
                    //将被标记的字段 组成map
                    Shp annotation = t.getAnnotation(Shp.class);
                    if (annotation != null) {
                        String name = t.getName();
                        String value = annotation.fieldName();
                        map.put(name, value);
                    }
                });
                if (!checkField(featureSource, desc)) {
                    log.error("数据字段不合法{}", map);
                    throw new ApiException("数据字段属性名称不合法,属性字段为->" + map);
                }
                break;
            }
        } finally {
            iterator.close();
            file.exists();
            sds.dispose();
        }
        return true;

    }

    /**
     * 检查字段名称是否匹配
     *
     * @param featureSource
     * @param tClass
     * @return
     */
    public static Boolean checkField(FeatureSource<SimpleFeatureType, SimpleFeature> featureSource, Class tClass) {
        Field[] declaredFields = tClass.getDeclaredFields();
        List<AttributeDescriptor> attributeDescriptors = featureSource.getSchema().getAttributeDescriptors();
        HashMap<String, String> map = new HashMap<>();
        for (AttributeDescriptor a : attributeDescriptors) {
            map.put(a.getName().toString(), a.getName().toString());
        }
        for (Field declaredField : declaredFields) {
            if (declaredField.getName().equals("geom") || declaredField.getName().equals("guid")) {
                continue;
            }
            //获取标记注解 标记名称
            Shp annotation = declaredField.getAnnotation(Shp.class);
            if (annotation == null) {
                continue;
            }
            if (map.get(declaredField.getName()) == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取shp文件类型
     *
     * @param file
     * @return
     */
    public static String getShpType(File file) throws IOException {
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore sds = null;
        SimpleFeatureIterator iterator = null;
        try {
            sds = (ShapefileDataStore) dataStoreFactory.createDataStore(file.toURI().toURL());
            sds.setCharset(Charset.forName("GBK"));
            iterator = sds.getFeatureSource().getFeatures().features();
            while (iterator.hasNext()) {
                SimpleFeature next = iterator.next();
                Object type = next.getAttribute("type");
                if (ObjectUtils.isEmpty(type)) {
                    throw new ApiException("shp文件没有type属性");
                }
                return type.toString();
            }
        } finally {
            iterator.close();
            file.exists();
            sds.dispose();
        }
        return null;
    }

    public Class getClazz() {
        return this.clazz;
    }

    //循环导入
    //  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void testSave(List<T> cacheData) {
        CompletableFuture.supplyAsync(() -> cacheData).thenApplyAsync((item) ->
                {

                    TransactionStatus transaction = dataSourceTransactionManager.getTransaction(transactionDefinition);
                    //int i = baseMapper.insertBatchSomeColumn(item);
                    dataSourceTransactionManager.commit(transaction);
                    return item;
                }
                , executor).exceptionally((ex) -> {
            //   JSONObject.parseObject()
            ex.printStackTrace();
            return null;
        });
    }

}

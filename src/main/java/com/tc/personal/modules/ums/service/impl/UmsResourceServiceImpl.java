package com.tc.personal.modules.ums.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tc.personal.modules.ums.mapper.UmsResourceMapper;
import com.tc.personal.modules.ums.model.UmsResource;
import com.tc.personal.modules.ums.service.UmsResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 后台资源管理Service实现类
 * Created by macro on 2020/2/2.
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService {
    @Autowired
    private UmsResourceMapper resourceMapper;


    @Override
    public List<UmsResource> listAll() {
        LambdaQueryWrapper<UmsResource> umsResourceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        return resourceMapper.selectList(umsResourceLambdaQueryWrapper);
    }
}

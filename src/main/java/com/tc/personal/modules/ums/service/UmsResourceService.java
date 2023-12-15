package com.tc.personal.modules.ums.service;

import com.tc.personal.modules.ums.model.UmsResource;

import java.util.List;

/**
 * 后台资源管理Service
 * Created by macro on 2020/2/2.
 */
public interface UmsResourceService {


    /**
     * 查询全部资源
     */
    List<UmsResource> listAll();
}

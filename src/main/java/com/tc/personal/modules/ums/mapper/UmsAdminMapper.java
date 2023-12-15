package com.tc.personal.modules.ums.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tc.personal.modules.ums.model.UmsAdmin;
import com.tc.personal.modules.ums.vo.UmsAdminVo;

/**
 * <p>
 * 账号表 Mapper 接口
 * </p>
 *
 * @author gw
 * @since 2023-05-19
 */
public interface UmsAdminMapper extends BaseMapper<UmsAdmin> {

    Page<UmsAdminVo> adminList(String keyword, Page<UmsAdmin> umsAdminVOPage);
}

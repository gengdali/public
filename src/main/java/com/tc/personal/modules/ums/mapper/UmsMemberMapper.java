package com.tc.personal.modules.ums.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tc.personal.modules.ums.model.UmsMember;

public interface UmsMemberMapper extends BaseMapper<UmsMember> {


    int deleteByPrimaryKey(Long id);

    int insertSelective(UmsMember record);

    UmsMember selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UmsMember record);

    int updateByPrimaryKey(UmsMember record);
}
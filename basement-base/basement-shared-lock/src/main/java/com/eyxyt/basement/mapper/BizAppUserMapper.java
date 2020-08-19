package com.eyxyt.basement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eyxyt.basement.entity.biz.BizAppUserEntity;
import com.eyxyt.basement.model.dto.UserListDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户信息表
 * @author cd.wang
 * @email 14163548
 * @date 2020-07-17 10:53:59
 */
public interface BizAppUserMapper extends BaseMapper<BizAppUserEntity> {


    List<BizAppUserEntity> selectPage(Page page, @Param("user") UserListDto userListDto);
}

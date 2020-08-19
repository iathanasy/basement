package com.eyxyt.basement.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.entity.biz.BizAppUserEntity;
import com.eyxyt.basement.exception.UnauthorizedException;
import com.eyxyt.basement.mapper.BizAppUserMapper;
import com.eyxyt.basement.model.dto.UserListDto;
import com.eyxyt.basement.service.BizAppUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("bizAppUserService")
public class BizAppUserServiceImpl extends ServiceImpl<BizAppUserMapper, BizAppUserEntity> implements BizAppUserService {

    @Override
    public BizAppUserEntity selectOne(String mobile) {
        BizAppUserEntity userEntity = new BizAppUserEntity();
        userEntity.setMobile(mobile);

        BizAppUserEntity bizAppUserEntity = this.getOne(new QueryWrapper<BizAppUserEntity>(userEntity));
        if (bizAppUserEntity == null) {
            throw new UnauthorizedException("该帐号不存在(The account does not exist.)");
        }
        return bizAppUserEntity;
    }

    @Override
    public PageVo queryPage(Query params) {
        Page page = this.page(params.getPage(), new QueryWrapper<BizAppUserEntity>());
        return new PageVo(page);
    }

    @Override
    public PageVo selectPage(Query params) {
        Page page = params.getPage();
        UserListDto userListDto = (UserListDto) params.getParams();

        List<BizAppUserEntity> bizAppUserEntities = baseMapper.selectPage(page, userListDto);
        page.setRecords(bizAppUserEntities);

        return new PageVo(page);
    }
}
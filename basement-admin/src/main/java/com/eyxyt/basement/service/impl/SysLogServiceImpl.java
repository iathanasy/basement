package com.eyxyt.basement.service.impl;

import cn.hutool.core.util.StrUtil;
import com.eyxyt.basement.entity.SysLogEntity;
import com.eyxyt.basement.mapper.SysLogMapper;
import com.eyxyt.basement.service.SysLogService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;


@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLogEntity> implements SysLogService {

    @Override
    public PageVo queryPage(Query params) {
        String key = params.getParams().getKey();
        Page page = this.page(params.getPage(),
                new QueryWrapper<SysLogEntity>()
                        .like(StrUtil.isNotBlank(key),"username", key));
        return new PageVo(page);
    }

}
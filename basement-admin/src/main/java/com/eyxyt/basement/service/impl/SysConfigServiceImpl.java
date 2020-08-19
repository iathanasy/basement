package com.eyxyt.basement.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eyxyt.basement.bean.PageVo;
import com.eyxyt.basement.bean.Query;
import com.eyxyt.basement.entity.SysConfigEntity;
import com.eyxyt.basement.exception.CustomException;
import com.eyxyt.basement.mapper.SysConfigMapper;
import com.eyxyt.basement.service.SysConfigService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;



@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements SysConfigService {


    @Override
    public PageVo queryPage(Query params) {
        String paramKey =  params.getParams().getKey();
        Page page = this.page(params.getPage(),
                new QueryWrapper<SysConfigEntity>()
                        .like(StrUtil.isNotBlank(paramKey),"param_key", paramKey));
        return new PageVo(page);
    }


    @Override
    public String getValue(String key) {
        SysConfigEntity config = null;
        if(config == null){
            config = baseMapper.queryByKey(key);
        }

        return config == null ? null : config.getParamValue();
    }

    @Override
    public <T> T getConfigObject(String key, Class<T> clazz) {
        String value = getValue(key);
        if(StrUtil.isNotBlank(value)){
            return new Gson().fromJson(value, clazz);
        }

        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new CustomException("获取参数失败");
        }
    }

}
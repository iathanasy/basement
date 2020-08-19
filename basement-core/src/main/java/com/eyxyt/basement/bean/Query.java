package com.eyxyt.basement.bean;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eyxyt.basement.xss.SQLFilter;
import lombok.Data;

import java.util.List;

/**
 * 分页查询参数
 * @author cd.wang
 * @create 2020-07-17 14:24
 * @since 1.0.0
 */
@Data
public class Query<T, V extends QueryParams> {
    /**
     * mybatis-plus分页参数
     */
    private Page<T> page;
    /**
     * 查询参数
     */
    private V params;

    public Query(V params) {
        this.params = params;
        //mybatis-plus分页
        this.page = new Page<T>(params.getCurrPage(), params.getPageSize());
        //排序
        if(StrUtil.isNotBlank(params.getOrderByColumn()) && StrUtil.isNotBlank(params.getIsAsc())){
            //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
            String orderField = SQLFilter.sqlInject(params.getOrderByColumn());

            List<OrderItem> orderItems = OrderItem.ascs(orderField);
            if("desc".equalsIgnoreCase(params.getIsAsc())){
                orderItems = OrderItem.descs(orderField);
            }
            this.page.setOrders(orderItems);
        }
    }
}

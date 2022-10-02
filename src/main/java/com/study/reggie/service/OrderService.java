package com.study.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.common.R;
import com.study.reggie.entity.OrderDetail;
import com.study.reggie.entity.Orders;

import java.util.List;
import java.util.Map;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
    public List<OrderDetail> getOrderDetailListByOrderId(Long orderId);

    Page getPage(int page, int pageSize);

    void submitAgain(Map<String, String> map);
}

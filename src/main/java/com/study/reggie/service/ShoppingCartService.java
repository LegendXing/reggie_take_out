package com.study.reggie.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entity.OrderDetail;
import com.study.reggie.entity.ShoppingCart;

public interface ShoppingCartService extends IService<ShoppingCart> {
    void clean();
}

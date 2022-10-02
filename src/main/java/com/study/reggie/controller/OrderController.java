package com.study.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.reggie.common.BaseContext;
import com.study.reggie.common.R;
import com.study.reggie.dto.OrderDto;
import com.study.reggie.entity.OrderDetail;
import com.study.reggie.entity.Orders;
import com.study.reggie.entity.ShoppingCart;
import com.study.reggie.service.OrderDetailService;
import com.study.reggie.service.OrderService;
import com.study.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 后台查询订单明细
     *
     * @param page
     * @param pageSize
     * @param number
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, String beginTime, String endTime) {
        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();


        //添加查询条件  动态sql  字符串使用StringUtils.isNotEmpty这个方法来判断
        queryWrapper.eq(number != null, Orders::getNumber, number).
                gt(StringUtils.isNotEmpty(beginTime), Orders::getOrderTime, beginTime).
                lt(StringUtils.isNotEmpty(endTime), Orders::getOrderTime, endTime);
        //这里使用了范围查询的动态SQL
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }


    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {

        Page pageDto = orderService.getPage(page, pageSize);
        return R.success(pageDto);
    }
    //客户端点击再来一单
    @PostMapping("/again")
    public R<String> againSubmit(@RequestBody Map<String,String> map){
        orderService.submitAgain(map);
        return R.success("操作成功");
    }
    @PutMapping
    public R<String> orderStatusChanged(@RequestBody Map<String,String> map){
        Long orderId = Long.parseLong(map.get("id"));
        Integer status = Integer.parseInt(map.get("status"));
        Orders orders = orderService.getById(orderId);
        orders.setStatus(status);
        orderService.updateById(orders);
        return R.success("订单状态修改成功");
    }
}
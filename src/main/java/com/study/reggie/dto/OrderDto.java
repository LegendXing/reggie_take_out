package com.study.reggie.dto;

import com.study.reggie.entity.OrderDetail;
import com.study.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrderDto extends Orders  {

    private List<OrderDetail> orderDetails;
}
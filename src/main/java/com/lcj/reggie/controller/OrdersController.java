package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.lcj.reggie.bean.Orders;
import com.lcj.reggie.common.R;
import com.lcj.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @PostMapping("/submit")
    public R<String> addOrder(@RequestBody Orders orders){
        ordersService.addOrder(orders);
        return R.success("添加订单成功");
    }
}

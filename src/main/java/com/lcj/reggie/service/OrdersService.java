package com.lcj.reggie.service;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcj.reggie.bean.Orders;

public interface OrdersService extends IService<Orders> {
    void addOrder(Orders orders);
}

package com.lcj.reggie.service;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lcj.reggie.bean.Orders;
import com.lcj.reggie.dto.OrdersDto;

public interface OrdersService extends IService<Orders> {
    void addOrder(Orders orders);

    Page<OrdersDto> getUserOrderPage(Integer page,Integer pageSize);

    void again(Orders orders);
}

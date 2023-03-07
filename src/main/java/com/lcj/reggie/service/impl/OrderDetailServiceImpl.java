package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.OrderDetail;
import com.lcj.reggie.mapper.OrderDetailMapper;
import com.lcj.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}

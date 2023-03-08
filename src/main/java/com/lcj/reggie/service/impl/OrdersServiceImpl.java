package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.*;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.CustomException;
import com.lcj.reggie.dto.OrdersDto;
import com.lcj.reggie.mapper.OrdersMapper;
import com.lcj.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void addOrder(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        User user = userService.getById(userId);

        //查询登陆用户的购物车数据
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        if(shoppingCartList==null || shoppingCartList.size()==0 ){
            throw new CustomException("购物车为空，不能下单！");
        }

        //查询用户地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());

        if(addressBook==null){
            throw new CustomException("地址为空，不能下单！");
        }

        //给订单对象设置信息
        long id = IdWorker.getId();
        orders.setId(id);
        orders.setNumber(String.valueOf(id));
        orders.setUserName(user.getName());
        orders.setUserId(userId);
        orders.setStatus(2);
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAddress((addressBook.getProvinceName() != null ? addressBook.getProvinceName() : "") +
                          (addressBook.getCityName() != null ? addressBook.getCityName() : "") +
                          (addressBook.getDistrictName() != null ? addressBook.getDistrictName() : "") +
                          (addressBook.getDetail() != null ? addressBook.getDetail() : "") );

        //计算订单金额并创建订单明细对象列表
        BigDecimal amount = new BigDecimal(0);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            //计算订单金额
            Integer number = shoppingCart.getNumber();
            BigDecimal shoppingCartAmount = shoppingCart.getAmount();
            amount=amount.add(shoppingCartAmount.multiply(new BigDecimal(number)));
            //创建订单明细对象
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail,"id");
            orderDetail.setOrderId(id);
            orderDetailList.add(orderDetail);
        }
        orders.setAmount(amount);
        //保存订单到数据库
        this.save(orders);

        //批量保存订单明细
        orderDetailService.saveBatch(orderDetailList);
        //清空下单用户的购物车
        shoppingCartService.remove(queryWrapper);
    }

    @Override
    public Page<OrdersDto> getUserOrderPage(Integer page, Integer pageSize) {

        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("user_id", BaseContext.getCurrentId());
        ordersQueryWrapper.orderByDesc("order_time");
        page(ordersPage,ordersQueryWrapper);

        List<Orders> records = ordersPage.getRecords();
        List<OrdersDto> ordersDtoList = new ArrayList<>();

        for (Orders record : records) {
            OrdersDto ordersDto = new OrdersDto();
            Long orderId = record.getId();
            QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
            orderDetailQueryWrapper.eq("order_id",orderId);
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailQueryWrapper);
            BeanUtils.copyProperties(record,ordersDto);
            ordersDto.setSumNum(orderDetails.size());
            ordersDto.setOrderDetails(orderDetails);
            ordersDtoList.add(ordersDto);
        }

        BeanUtils.copyProperties(ordersPage,ordersDtoPage,"records");
        ordersDtoPage.setRecords(ordersDtoList);

        return ordersDtoPage;
    }

    @Transactional
    @Override
    public void again(Orders orders) {
        Long ordersId = orders.getId();
        Orders orders1 = getById(ordersId);

        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.eq("order_id",ordersId);
        List<OrderDetail> list = orderDetailService.list(orderDetailQueryWrapper);

        long id = IdWorker.getId();
        orders1.setId(id);
        orders1.setNumber(String.valueOf(id));
        orders1.setOrderTime(LocalDateTime.now());
        orders1.setCheckoutTime(LocalDateTime.now());
        orders1.setStatus(2);
        save(orders1);

        for (OrderDetail orderDetail : list) {
            orderDetail.setId(null);
            orderDetail.setOrderId(id);
        }

        orderDetailService.saveBatch(list);
    }
}

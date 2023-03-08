package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcj.reggie.bean.Orders;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.OrdersDto;
import com.lcj.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> page(@RequestParam("page") Integer page,
                                   @RequestParam("pageSize") Integer pageSize){
        Page<OrdersDto> userOrderPage = ordersService.getUserOrderPage(page, pageSize);
        return R.success(userOrderPage);
    }

    @PostMapping("/again")
    public R<String> again(@RequestBody Orders orders){
        ordersService.again(orders);
        return R.success("再来一单成功");
    }

    @GetMapping("/page")
    public R<Page<Orders>> page(@RequestParam("page") Integer page,
                                @RequestParam("pageSize") Integer pageSize,
                                @RequestParam(value = "number",defaultValue = "") Long number,
                                @RequestParam(value = "beginTime",defaultValue = "") String beginTime,
                                @RequestParam(value = "endTime",defaultValue = "") String endTime){
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq(number!=null,"number",number);
        ordersQueryWrapper.between(StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime),"order_time",beginTime,endTime);
        ordersQueryWrapper.orderByDesc("order_time").orderByAsc("status");
        ordersService.page(ordersPage, ordersQueryWrapper);
        return R.success(ordersPage);
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody Orders orders){
        ordersService.updateById(orders);
        return R.success("修改成功");
    }
}

package com.lcj.reggie.bean;/*
    @author lcj
    @create -
*/

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetail {
    private Long id;
    private String name;
    private String image;
    private Long orderId;
    private Long dishId;
    private Long setmealId;
    private String dishFlavor;
    private Integer number;
    private BigDecimal amount;
}

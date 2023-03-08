package com.lcj.reggie.dto;

import com.lcj.reggie.bean.OrderDetail;
import com.lcj.reggie.bean.Orders;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.List;

@Data
public class OrdersDto extends Orders {
    private Integer sumNum;

    private List<OrderDetail> orderDetails;
}

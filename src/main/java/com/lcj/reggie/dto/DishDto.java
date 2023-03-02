package com.lcj.reggie.dto;/*
    @author lcj
    @create -
*/

import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.bean.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishDto extends Dish {
    private List<DishFlavor> flavors;
}

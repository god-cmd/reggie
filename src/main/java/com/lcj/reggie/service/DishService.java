package com.lcj.reggie.service;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.DishDto;

public interface DishService extends IService<Dish> {
    R<String> saveWithFlavor(DishDto dishDto);
}

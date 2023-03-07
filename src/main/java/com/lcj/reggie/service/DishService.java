package com.lcj.reggie.service;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.bean.DishFlavor;
import com.lcj.reggie.bean.User;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.DishDto;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    List<DishFlavor> getDishFlavorById(Long id);

    void updateWithFlavor(DishDto dishDto);

    void removeBatch(List<Long> ids);

    void updateStatus(Integer status,List<Long> ids);
}

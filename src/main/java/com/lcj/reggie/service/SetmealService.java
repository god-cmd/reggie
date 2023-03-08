package com.lcj.reggie.service;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.bean.SetmealDish;
import com.lcj.reggie.dto.SetmealDishDto;
import com.lcj.reggie.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveSetmealAndDish(SetmealDto dto);

    SetmealDto getSetmealId(Long id);

    void updateSetmealAndDish(SetmealDto dto);

    void removeSetmealAndDish(List<Long> ids);

    List<SetmealDishDto> getSetmealDishById(Long setmealId);
}

package com.lcj.reggie.service;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.IService;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.dto.SetmealDto;

public interface SetmealService extends IService<Setmeal> {
    void saveSetmealAndDish(SetmealDto dto);
}

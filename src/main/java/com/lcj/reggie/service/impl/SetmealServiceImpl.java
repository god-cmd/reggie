package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.bean.SetmealDish;
import com.lcj.reggie.dto.SetmealDto;
import com.lcj.reggie.mapper.SetmealMapper;
import com.lcj.reggie.service.SetmealDishService;
import com.lcj.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveSetmealAndDish(SetmealDto dto) {
        save(dto);

        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(dto.getId());
            setmealDishService.save(setmealDish);
        }
    }
}

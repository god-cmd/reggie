package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.bean.DishFlavor;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.DishDto;
import com.lcj.reggie.mapper.DishMapper;
import com.lcj.reggie.service.DishFlavorService;
import com.lcj.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        save(dishDto);
//        int i = 1/0;
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorService.save(flavor);
        }

    }

    @Override
    public List<DishFlavor> getDishFlavorById(Long id) {
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dish_id",id);
        return dishFlavorService.list(queryWrapper);
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        Long id = dishDto.getId();
        updateById(dishDto);

        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper();
        queryWrapper.eq("dish_id",id);
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
            dishFlavorService.save(flavor);
        }
    }

    @Transactional
    @Override
    public void removeBatch(List<Long> ids) {
        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        for (Long id : ids) {
            queryWrapper.eq("dish_id",id);
            dishFlavorService.remove(queryWrapper);
        }

        removeByIds(ids);
    }
}

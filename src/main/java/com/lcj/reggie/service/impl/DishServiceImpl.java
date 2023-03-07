package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.bean.DishFlavor;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.bean.SetmealDish;
import com.lcj.reggie.common.CustomException;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.DishDto;
import com.lcj.reggie.mapper.DishMapper;
import com.lcj.reggie.service.DishFlavorService;
import com.lcj.reggie.service.DishService;
import com.lcj.reggie.service.SetmealDishService;
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
    @Autowired
    private SetmealDishService setmealDishService;

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
        queryWrapper.eq("dish_id", id);
        return dishFlavorService.list(queryWrapper);
    }

    @Transactional
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        Long id = dishDto.getId();
        updateById(dishDto);

        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper();
        queryWrapper.eq("dish_id", id);
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

        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.in("id", ids).eq("status", 1);
        List<Dish> list = this.list(dishQueryWrapper);

        if (list.size() > 0) {
            throw new CustomException("存在已起售商品，不能删除");
        }

        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        for (Long id : ids) {
            dishFlavorQueryWrapper.eq("dish_id", id);
            setmealDishQueryWrapper.eq("dish_id", id);
            dishFlavorService.remove(dishFlavorQueryWrapper);
            setmealDishService.remove(setmealDishQueryWrapper);
        }

        removeByIds(ids);
    }

    @Override
    public void updateStatus(Integer status, List<Long> ids) {
        UpdateWrapper<SetmealDish> setmealDishUpdateWrapper = new UpdateWrapper<>();
        setmealDishUpdateWrapper.set(status == 0, "is_deleted", 1);
        setmealDishUpdateWrapper.set(status == 1, "is_deleted", 0);
        setmealDishUpdateWrapper.in("dish_id", ids);
        setmealDishService.update(setmealDishUpdateWrapper);

        UpdateWrapper<Dish> dishUpdateWrapper = new UpdateWrapper<>();
        dishUpdateWrapper.set("status", status);
        dishUpdateWrapper.in("id", ids);
        update(dishUpdateWrapper);
    }


}

package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.Category;
import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.common.CustomException;
import com.lcj.reggie.common.R;
import com.lcj.reggie.mapper.CategoryMapper;
import com.lcj.reggie.service.CategoryService;
import com.lcj.reggie.service.DishService;
import com.lcj.reggie.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;

    public R<String> remove(Long id){
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq("category_id",id).last("limit 1");
        Dish one = dishService.getOne(dishQueryWrapper);
        if(one!=null){
            throw new CustomException("该分类存在关联的菜品，不能删除");
        }
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.eq("category_id",id).last("limit 1");
        Setmeal two = setMealService.getOne(setmealQueryWrapper);
        if(two!=null){
            throw new CustomException("该分类存在关联的套餐，不能删除");
        }
        removeById(id);
        return R.success("删除成功");
    }

}

package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lcj.reggie.bean.Category;
import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.bean.DishFlavor;
import com.lcj.reggie.dto.DishDto;
import com.lcj.reggie.service.CategoryService;
import com.lcj.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.lcj.reggie.common.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public R<List<DishDto>> list(@RequestParam(value = "categoryId",defaultValue = "") Long categoryId,
                              @RequestParam(value = "name",defaultValue = "") String name,
                              @RequestParam(value = "status",defaultValue = "") Integer status){
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq("status",1);
        wrapper.eq(categoryId!=null,"category_id",categoryId);
        wrapper.eq(status!=null,"status",status);
        wrapper.like(Strings.isNotEmpty(name),"name",name);
        List<Dish> list = dishService.list(wrapper);

        List<DishDto> dtoList = new ArrayList<>();
        for (Dish dish : list) {
            DishDto dishDto = new DishDto();
            List<DishFlavor> dishFlavorById = dishService.getDishFlavorById(dish.getId());
            dishDto.setFlavors(dishFlavorById);
            BeanUtils.copyProperties(dish,dishDto);
            dtoList.add(dishDto);
        }

        return R.success(dtoList);
    }

    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        log.info("添加菜品:{}",dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @PutMapping
    public R<String> updateDish(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateDishStatus(@PathVariable("status") Integer status,
                                      @RequestParam("ids") List<Long> ids){
        dishService.updateStatus(status,ids);
        return R.success("修改成功");
    }

    @DeleteMapping
    public R<String> deleteDishById(@RequestParam("ids") List<Long> ids){
        dishService.removeBatch(ids);
        return R.success("删除成功");
    }

    @GetMapping("/{id}")
    public R<DishDto> getDishById(@PathVariable("id") Long id){
        Dish dish = dishService.getById(id);
        List<DishFlavor> dishFlavor = dishService.getDishFlavorById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(dishFlavor);
        return R.success(dishDto);
    }

    @GetMapping("/page")
    public R<Page> page(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize,
                        @RequestParam(value = "name",defaultValue = Strings.EMPTY) String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page,pageSize);

        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort").orderByDesc("update_time");
        queryWrapper.like(!name.isEmpty(),"name",name);
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = new ArrayList<>();
        for (Dish dish : records) {
            DishDto dishDto = new DishDto();
            Long categoryId = dish.getCategoryId();
            Category category = categoryService.getById(categoryId);
            dishDto.setCategoryName(category.getName());
            BeanUtils.copyProperties(dish,dishDto);
            list.add(dishDto);
        }
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }
}

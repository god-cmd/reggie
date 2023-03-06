package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lcj.reggie.bean.Category;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.bean.SetmealDish;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.SetmealDto;
import com.lcj.reggie.service.CategoryService;
import com.lcj.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService SetmealService;
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        SetmealService.saveSetmealAndDish(setmealDto);
        return R.success("添加成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealById(@PathVariable("id") Long id){
        SetmealDto setmealDto = SetmealService.getSetmealId(id);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        SetmealService.updateSetmealAndDish(setmealDto);
        return R.success("修改成功");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(@RequestParam("page") Integer page,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @RequestParam(value = "name",defaultValue = "") String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page,pageSize);

        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(name),"name",name);
        SetmealService.page(pageInfo,wrapper);

        BeanUtils.copyProperties(pageInfo,setmealDtoPage);

        List<Setmeal> setmealList = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        for (Setmeal setmeal : setmealList) {
            SetmealDto setmealDto = new SetmealDto();
            Long categoryId = setmeal.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            BeanUtils.copyProperties(setmeal,setmealDto);
            setmealDtoList.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> removeSetmeal(@RequestParam("ids") List<Long> ids){
        SetmealService.removeSetmealAndDish(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status,
                                  @RequestParam("ids") List<Long> ids){
        UpdateWrapper<Setmeal> setmealUpdateWrapper = new UpdateWrapper<>();
        setmealUpdateWrapper.set("status",status);
        setmealUpdateWrapper.in("id",ids);
        SetmealService.update(setmealUpdateWrapper);
        return R.success("修改成功");
    }
}

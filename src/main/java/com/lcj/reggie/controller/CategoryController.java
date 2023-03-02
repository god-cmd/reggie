package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lcj.reggie.bean.Category;
import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.common.R;
import com.lcj.reggie.service.CategoryService;
import com.lcj.reggie.service.DishService;
import com.lcj.reggie.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(@RequestParam("page") Integer page,
                        @RequestParam("pageSize") Integer pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByAsc("sort");
        categoryService.page(pageInfo,wrapper);
        return R.success(pageInfo);
    }

    @PostMapping
    public R<String> saveCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }

    @DeleteMapping
    public R<String> deleteCategory(@RequestParam("ids") Long id){
        return categoryService.remove(id);
    }

    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }
}

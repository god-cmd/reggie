package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.lcj.reggie.bean.Dish;
import com.lcj.reggie.dto.DishDto;
import com.lcj.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import com.lcj.reggie.common.R;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> saveDish(@RequestBody DishDto dishDto){
        log.info("添加菜品:{}",dishDto);
        return dishService.saveWithFlavor(dishDto);
    }
}

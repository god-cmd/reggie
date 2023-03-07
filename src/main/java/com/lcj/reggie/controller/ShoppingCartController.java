package com.lcj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcj.reggie.bean.ShoppingCart;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.R;
import com.lcj.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id",userId);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartQueryWrapper);
        return R.success(list);
    }

}

package com.lcj.reggie.controller;

import ch.qos.logback.core.status.WarnStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcj.reggie.bean.ShoppingCart;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.R;
import com.lcj.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart cart){
        log.info("cart:{}",cart);
        Long userId = BaseContext.getCurrentId();
        cart.setUserId(userId);

        Long dishId = cart.getDishId();
        Long setmealId = cart.getSetmealId();

        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq(dishId!=null,"dish_id",dishId);
        queryWrapper.eq(setmealId!=null,"setmeal_id",setmealId);
        ShoppingCart shoppingCart = shoppingCartService.getOne(queryWrapper);

        if(shoppingCart==null){
            cart.setNumber(1);
            shoppingCartService.save(cart);
            shoppingCart = cart;
        }else{
            Integer number = shoppingCart.getNumber()+1;
            shoppingCart.setNumber(number);
//            shoppingCart.setAmount(shoppingCart.getAmount().multiply(BigDecimal.valueOf(number)));
            shoppingCartService.updateById(shoppingCart);
        }

        return R.success(shoppingCart);
    }

    @DeleteMapping("clean")
    public R<String> clear(){
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }

    @PostMapping("/sub")
    public R<ShoppingCart> reduce(@RequestBody ShoppingCart shoppingCart){
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        Long userId = BaseContext.getCurrentId();

        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq(dishId!=null,"dish_id",dishId);
        wrapper.eq(setmealId!=null,"setmeal_id",setmealId);
        ShoppingCart sc = shoppingCartService.getOne(wrapper);

        sc.setNumber(sc.getNumber()-1);
        if(sc.getNumber()==0){
            shoppingCartService.removeById(sc.getId());
        }else{
            shoppingCartService.updateById(sc);
        }
        return R.success(sc);
    }
}

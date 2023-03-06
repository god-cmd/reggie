package com.lcj.reggie.service.impl;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.bean.SetmealDish;
import com.lcj.reggie.common.CustomException;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.SetmealDto;
import com.lcj.reggie.mapper.SetmealMapper;
import com.lcj.reggie.service.SetmealDishService;
import com.lcj.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Override
    public SetmealDto getSetmealId(Long id) {
        Setmeal setmeal = getById(id);

        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted",0);
        queryWrapper.eq(setmeal != null,"setmeal_id",id);
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);

        return setmealDto;
    }

    @Transactional
    @Override
    public void updateSetmealAndDish(SetmealDto dto) {
        updateById(dto);

        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id",dto.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = dto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(dto.getId());
            setmealDishService.save(setmealDish);
        }
    }

    @Transactional
    @Override
    public void removeSetmealAndDish(List<Long> ids) {
        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        setmealQueryWrapper.in("id",ids).eq("status",1);
        List<Setmeal> list = this.list(setmealQueryWrapper);

        if(list.size()>0){
            throw new CustomException("存在已起售商品，不能删除");
        }

        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        for (Long id : ids) {
            setmealDishQueryWrapper.eq("setmeal_id",id);
            setmealDishService.remove(setmealDishQueryWrapper);
        }

        removeByIds(ids);
    }

}

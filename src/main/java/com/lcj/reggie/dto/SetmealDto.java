package com.lcj.reggie.dto;/*
    @author lcj
    @create -
*/

import com.lcj.reggie.bean.Setmeal;
import com.lcj.reggie.bean.SetmealDish;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;

}

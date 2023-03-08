package com.lcj.reggie.dto;/*
    @author lcj
    @create -
*/

import com.lcj.reggie.bean.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String email;
}

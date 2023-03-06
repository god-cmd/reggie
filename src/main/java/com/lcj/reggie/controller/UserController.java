package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcj.reggie.bean.User;
import com.lcj.reggie.common.R;
import com.lcj.reggie.service.UserService;
import com.lcj.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            log.info("code:{}",code);
            session.setAttribute(phone,code);
            return R.success("验证码发送成功");
        }
        return R.error("发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        String phone = map.get("phone").toString();
        String code = session.getAttribute(phone).toString();
        String sendCode = map.get("code").toString();

        if(StringUtils.isNotEmpty(sendCode) && sendCode.equals(code)){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("phone",phone);
            User user = userService.getOne(userQueryWrapper);

            if(user == null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        }else{
            return R.error("验证码错误");
        }
    }
}

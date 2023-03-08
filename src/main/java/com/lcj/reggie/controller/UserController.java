package com.lcj.reggie.controller;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lcj.reggie.bean.User;
import com.lcj.reggie.common.CustomException;
import com.lcj.reggie.common.R;
import com.lcj.reggie.dto.UserDto;
import com.lcj.reggie.service.UserService;
import com.lcj.reggie.utils.ValidateCodeUtils;
import com.sun.mail.iap.ParsingException;
import com.sun.mail.imap.protocol.MailboxInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody UserDto user, HttpSession session) throws ParsingException {
          String email = user.getEmail();

          if(StringUtils.isNotEmpty(email)){
              Integer code = ValidateCodeUtils.generateValidateCode(4);
              SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
              simpleMailMessage.setFrom("1920794127@qq.com");
              simpleMailMessage.setTo(email);
              simpleMailMessage.setSubject("验证码");
              simpleMailMessage.setText("请收下你的验证码："+code);
              javaMailSender.send(simpleMailMessage);
              session.setAttribute(user.getPhone(),code);
              return R.success("验证码发送成功");
          }
          return R.error("发送失败");
//        String phone = user.getPhone();
//
//        if (StringUtils.isNotEmpty(phone)) {
//            Integer code = ValidateCodeUtils.generateValidateCode(4);
//            log.info("code:{}",code);
//
//            session.setAttribute(phone,code);
//            return R.success("验证码发送成功");
//        }
//        return R.error("发送失败");
    }

    @PostMapping("loginout")
    public R<String> logout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        String phone = map.get("phone").toString();
        String sendCode = map.get("code").toString();

        if(session.getAttribute(phone) == null){
            throw new CustomException("请获取新的验证码");
        }
        String code = session.getAttribute(phone).toString();

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

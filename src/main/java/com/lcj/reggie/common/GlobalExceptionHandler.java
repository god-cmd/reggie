package com.lcj.reggie.common;/*
    @author lcj
    @create -
*/

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler{
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        String message = ex.getMessage();
        log.info("捕获的异常信息：{}",message);
        if(message.contains("Duplicate entry")){
            String[] split = message.split(" ");
            String msg = split[2]+"已存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R<String> removeExceptionHandler(CustomException ex){
        return R.error(ex.getMessage());
    }

}

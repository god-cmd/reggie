package com.lcj.reggie.common;/*
    @author lcj
    @create -
*/

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    HttpSession session;

    @Override
    public void insertFill(MetaObject metaObject) {
        Long currentId = BaseContext.getCurrentId();
        log.info("正在插入的对象：{}",metaObject.toString());
//        metaObject.setValue("createTime",LocalDateTime.now());
        this.strictInsertFill(metaObject,"createTime",LocalDateTime.class,LocalDateTime.now());
//        metaObject.setValue("createUser",currentId);
        this.strictInsertFill(metaObject,"createUser",Long.class,currentId);
        this.strictInsertFill(metaObject,"updateTime",LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"updateUser",Long.class,currentId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentId = BaseContext.getCurrentId();
        log.info("正在修改的对象：{}",metaObject.toString());
        this.strictInsertFill(metaObject,"updateTime",LocalDateTime.class,LocalDateTime.now());
        this.strictInsertFill(metaObject,"updateUser",Long.class,currentId);
    }
}

package com.lcj.reggie;

import lombok.extern.slf4j.Slf4j;
import org.apache.naming.factory.SendMailFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.annotation.Repeat;

@Slf4j
@SpringBootTest
public class EmailTest {
    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    public void testSendEmail(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("1920794127@qq.com");
        simpleMailMessage.setTo("1373761536@qq.com");
        simpleMailMessage.setSubject("李尚隆");
        simpleMailMessage.setText("傻逼");
        javaMailSender.send(simpleMailMessage);
    }
}

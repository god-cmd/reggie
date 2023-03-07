package com.lcj.reggie.interceptor;/*
    @author lcj
    @create -
*/

import com.alibaba.fastjson.JSON;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object employee = session.getAttribute("employee");
        String referer = request.getHeader("Referer");
        log.info("referer:{}",referer);
        if (employee != null && referer.contains("backend")){
            BaseContext.setCurrentId((Long) employee);
            return true;
        }
        Object user = session.getAttribute("user");
        if (referer.contains("front") && user != null){
            BaseContext.setCurrentId((Long) user);
            return true;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }


}

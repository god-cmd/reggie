package com.lcj.reggie.interceptor;/*
    @author lcj
    @create -
*/

import com.alibaba.fastjson.JSON;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.R;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object employee = request.getSession().getAttribute("employee");
        if (employee == null){
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
        BaseContext.setCurrentId((Long) employee);
        return true;
    }


}

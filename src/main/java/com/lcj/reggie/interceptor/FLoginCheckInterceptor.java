package com.lcj.reggie.interceptor;/*
    @author lcj
    @create -
*/

import com.alibaba.fastjson.JSON;
import com.lcj.reggie.common.BaseContext;
import com.lcj.reggie.common.R;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FLoginCheckInterceptor implements HandlerInterceptor {

    public static final String STATUS = "INTERCEPTOR_HANDLER_STATUS";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object user = session.getAttribute("user");
        Integer status = (Integer) session.getAttribute(STATUS);
        if (status == null) {
            status=0;
            session.setAttribute(STATUS,status);
        }
        if(status == 1){
            session.setAttribute(STATUS,0);
            return false;
        }
        if (user == null){
            session.setAttribute(STATUS,status+1);
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }

        session.setAttribute("status",0);

        BaseContext.setCurrentId((Long) user);
        return true;
    }
}

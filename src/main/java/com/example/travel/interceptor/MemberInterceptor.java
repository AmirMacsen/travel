package com.example.travel.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class MemberInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从session中获取用户信息
        Object member = request.getSession().getAttribute("member");
        if (null == member){
            response.sendRedirect(request.getContextPath()+"/frontdesk/login");
            return false;
        }
        return true;
    }
}

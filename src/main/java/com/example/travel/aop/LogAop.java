package com.example.travel.aop;

import com.example.travel.bean.Log;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Aspect
public class LogAop {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private final static Logger logger = LoggerFactory.getLogger(LogAop.class);

    @Pointcut("execution(* com.example.travel.controller.backstage.*.*(..))")
    public void log() {}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        // 1. 记录访问时间
        Date visitTime = new Date();
        request.setAttribute("visitTime", visitTime);
    }

    @After("log()")
    public void doAfter(JoinPoint joinPoint) {
        Log log = new Log();
        Date visitTime = (Date) request.getAttribute("visitTime");
        Date now = new Date();
        log.setVisitTime(visitTime);
        log.setExecutionTime((int)(now.getTime() - visitTime.getTime()));
        log.setUrl(request.getRequestURI());
        log.setIp(request.getRemoteAddr());
        // 拿到Security中的User对象
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof User) {
            log.setUsername(user.toString());
        }
        logger.info(log.toString());
    }

    @AfterThrowing(pointcut = "log()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
        Log log = new Log();
        Date visitTime = (Date) request.getAttribute("visitTime");
        Date now = new Date();
        log.setVisitTime(visitTime);
        log.setExecutionTime((int)(now.getTime() - visitTime.getTime()));
        log.setUrl(request.getRequestURI());
        log.setIp(request.getRemoteAddr());
        // 拿到Security中的User对象
        Object user= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user instanceof User) {
            log.setUsername(user.toString());
        }
        log.setExceptionMessage(ex.getMessage());
        logger.info(log.toString());
    }
}

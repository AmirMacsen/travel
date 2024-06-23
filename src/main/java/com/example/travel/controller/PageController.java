package com.example.travel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PageController {
    //  访问后台页面
    @RequestMapping("/backstage/{page}")
    public String backstage(@PathVariable("page") String page) {
        return "/backstage/" + page;
    }

    //  访问前台页面
    @RequestMapping("/frontdesk/{page}")
    public String front(@PathVariable("page") String page) {
        return "/frontdesk/" + page;
    }

    // ico 项目logo
    @RequestMapping("favicon.ico")
    @ResponseBody
    public void icon() {

    }
}

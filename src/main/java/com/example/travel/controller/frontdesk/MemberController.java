package com.example.travel.controller.frontdesk;

import com.example.travel.bean.Result;
import com.example.travel.pojo.Member;
import com.example.travel.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/frontdesk/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/register")
    public ModelAndView register(Member member, String checkCode, HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        String sessionCheckCode = (String) httpSession.getAttribute("checkCode");
        if (!sessionCheckCode.equalsIgnoreCase(checkCode)) {
            modelAndView.addObject("message", "验证码错误");
            modelAndView.setViewName("/frontdesk/register");
            return modelAndView;
        }
        Result result = memberService.register(member);
        if (!result.isFlag()){
            modelAndView.addObject("message", result.getMessage());
            modelAndView.setViewName("/frontdesk/register");
            return modelAndView;
        }else{
            modelAndView.setViewName("/frontdesk/register_ok");
            return modelAndView;
        }
    }

    @RequestMapping("/active")
    public ModelAndView active(String activeCode) {
        ModelAndView modelAndView = new ModelAndView();
        String message = memberService.active(activeCode);
        modelAndView.addObject("message", message);
        modelAndView.setViewName("/frontdesk/active_result");
        return modelAndView;
    }

    @RequestMapping("/login")
    public ModelAndView register(String name,String password,HttpSession session){
        ModelAndView modelAndView = new ModelAndView();
        Result result = memberService.login(name,password);
        if(!result.isFlag()){ // 登录失败
            modelAndView.addObject("message",result.getMessage());
            modelAndView.setViewName("/frontdesk/login");
        }else{ // 登录成功
            session.setAttribute("member",result.getData()); // 将用户信息存入session
            modelAndView.setViewName("redirect:/frontdesk/index");
        }
        return modelAndView;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("member");
        return "redirect:/frontdesk/index";
    }


}

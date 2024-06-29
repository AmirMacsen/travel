package com.example.travel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.travel.bean.Result;
import com.example.travel.mapper.MemberMapper;
import com.example.travel.pojo.Member;
import com.example.travel.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private MailUtils mailUtils;
    @Value("${project.path}")
    private String path;

    public Result register(Member member){
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",member.getUsername());

        List<Member> members = memberMapper.selectList(queryWrapper);
        if (!members.isEmpty()) {
            return new Result(false, "用户名已存在");
        }

        QueryWrapper<Member> queryWrapper1 = new QueryWrapper<>();
        queryWrapper.eq("phoneNum", member.getPhoneNum());
        List<Member> members1 = memberMapper.selectList(queryWrapper1);
        if (!members1.isEmpty()) {
            return new Result(false, "手机号已存在");
        }

        QueryWrapper<Member> queryWrapper2 = new QueryWrapper<>();
        queryWrapper.eq("email", member.getEmail());
        List<Member> members2 = memberMapper.selectList(queryWrapper2);
        if (!members2.isEmpty()) {
            return new Result(false, "邮箱已存在");
        }

        // 发送激活邮件
        String activeCode = UUID.randomUUID().toString();
        // 给用户的邮箱发送一封邮件，该邮件包含一个链接，链接中包含激活码
        String activeUrl = path + "frontdesk/member/active?activeCode=" + activeCode;
        String text = "恭喜您注册成功！<a href = '" + activeUrl + "'>点击激活</a>完成账号认证";
        mailUtils.sendMail(member.getEmail(), text, "旅游网激活邮件");
        member.setActiveCode(activeCode);
        member.setPassword(encoder.encode(member.getPassword()));
        member.setActive(false);
        memberMapper.insert(member);

        return new Result(true, "注册成功");
    }

    // 激活用户
    public String active(String activeCode){
        // 根据激活码查询用户
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("activeCode",activeCode);
        Member member = memberMapper.selectOne(queryWrapper);
        // 没有找到用户：激活失败
        if(member == null){
            return "激活失败！激活码错误!";
        }else {
            member.setActive(true);
            memberMapper.updateById(member);
            return "激活成功，请<a href='"+path+"frontdesk/login'>登录</a>";
        }
    }

    public Result login(String name,String password){
        Member member = null;

        // 查询用户名
        if (member == null){
            QueryWrapper<Member> queryWrapper = new QueryWrapper();
            queryWrapper.eq("username",name);
            member = memberMapper.selectOne(queryWrapper);
        }

        // 查询手机
        if (member == null){
            QueryWrapper<Member> queryWrapper = new QueryWrapper();
            queryWrapper.eq("phoneNum",name);
            member = memberMapper.selectOne(queryWrapper);
        }

        // 查询邮箱
        if (member == null){
            QueryWrapper<Member> queryWrapper = new QueryWrapper();
            queryWrapper.eq("email",name);
            member = memberMapper.selectOne(queryWrapper);
        }


        // 没有查询到用户
        if(member == null){
            return new Result(false,"用户名或密码错误");
        }

        // 验证密码
        boolean flag = encoder.matches(password, member.getPassword());
        if(!flag){
            return new Result(false,"用户名或密码错误");
        }

        // 验证是否激活
        if(!member.isActive()){
            return new Result(false,"用户未激活，请登录邮箱激活用户！");
        }

        return new Result(true,"登录成功！",member);
    }

}


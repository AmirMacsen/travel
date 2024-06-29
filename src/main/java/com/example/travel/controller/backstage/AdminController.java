package com.example.travel.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.pojo.Admin;
import com.example.travel.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.method.AuthorizationManagerBeforeMethodInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/backstage/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @RequestMapping("/all")
    @PreAuthorize("hasAuthority('/admin/all')")
    public ModelAndView all(@RequestParam(value = "page", defaultValue = "1")int page,
                            @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Admin> selectedPage = adminService.findPage(page, size);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("adminPage", selectedPage);
        modelAndView.setViewName("backstage/admin_all");
        return modelAndView;
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('/admin/add')")
    public String add(Admin admin){
        adminService.add(admin);
        return "redirect:/backstage/admin/all";
    }

    // 查询管理员并跳转到修改页面
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('/admin/edit')")
    public ModelAndView edit(int aid){
        Admin admin = adminService.findAdminById(aid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("admin", admin);
        modelAndView.setViewName("backstage/admin_edit");
         return modelAndView;
    }

    // 修改管理员
    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('/admin/update')")
    public String update(Admin admin){
        adminService.update(admin);
        return "redirect:/backstage/admin/all";
    }

    // 管理员详情
    @RequestMapping("/desc")
    @PreAuthorize("hasAuthority('/admin/desc')")
    public ModelAndView desc(int aid){
        Admin admin = adminService.findDesc(aid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("admin", admin);
        modelAndView.setViewName("backstage/admin_desc");
        return modelAndView;
    }

    // 查询用户角色
    @RequestMapping("/findRole")
    @PreAuthorize("hasAuthority('/admin/findRole')")
    public ModelAndView findRole(int aid){
        ModelAndView modelAndView = new ModelAndView();
        System.out.println(adminService.findRole(aid));
        modelAndView.addObject("roles", adminService.findRole(aid));
        modelAndView.addObject("aid", aid);
        modelAndView.setViewName("backstage/admin_role");
        return modelAndView;
    }

    // 修改用户角色
    @RequestMapping("/updateRole")
    @PreAuthorize("hasAuthority('/admin/updateRole')")
    public String updateRole(int aid, @RequestParam(value = "roleIds", defaultValue = "") List<Integer> roleIds){
        adminService.updateRole(aid, roleIds);
        return "redirect:/backstage/admin/all";
    }

    // 修改用户状态
    @RequestMapping("/updateStatus")
    @PreAuthorize("hasAuthority('/admin/updateStatus')")
    public String updateStatus(int aid){
        adminService.updateStatus(aid);
        return "redirect:/backstage/admin/all";
    }
}

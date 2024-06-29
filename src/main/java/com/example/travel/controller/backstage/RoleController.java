package com.example.travel.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.bean.PermissionWithStatus;
import com.example.travel.pojo.Role;
import com.example.travel.service.RoleService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/backstage/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private HikariDataSource dataSource;

    // 查询所有
    @RequestMapping("/all")
    @PreAuthorize("hasAuthority('/role/all')")
    public ModelAndView findAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                @RequestParam(value = "size", defaultValue = "10") int size){
        Page<Role> roleList = roleService.findPage(page, size);
        return new ModelAndView("backstage/role_all", "roleList", roleList);
    }

    // 添加
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('/role/add')")
    public String add(Role role){
        roleService.add(role);
        return "redirect:/backstage/role/all";
    }

    // 查找角色
    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('/role/edit')")
    public ModelAndView findRoleById(int rid){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("role", roleService.findRoleById(rid));
        modelAndView.setViewName("backstage/role_edit");
        return modelAndView;
    }

    // 修改

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('/role/update')")
    public String update(Role role){
        roleService.update(role);
        return "redirect:/backstage/role/all";
    }

    // 删除角色
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('/role/delete')")
    public String delete(int rid){
        roleService.delete(rid);
        return "redirect:/backstage/role/all";
    }

    // 查询角色的权限
    @RequestMapping("/findPermission")
    @PreAuthorize("hasAuthority('/role/findPermission')")
    public ModelAndView findPermission(int rid){
        ModelAndView modelAndView = new ModelAndView();
        List<PermissionWithStatus> permissionWithStatusList = roleService.findPermission(rid);
        modelAndView.addObject("permissionWithStatusList", permissionWithStatusList);
        modelAndView.addObject("rid", rid);
        modelAndView.setViewName("backstage/role_permission");
        return modelAndView;
    }

    // 修改角色的权限
    @RequestMapping("/updatePermission")
    @PreAuthorize("hasAuthority('/role/updatePermission')")
    public String updateRolePermission(int rid, @RequestParam(value = "ids", defaultValue = "") List<Integer> ids){
        System.out.println(rid);
        System.out.println(ids);
        roleService.updateRolePermission(rid, ids);
        return "redirect:/backstage/role/all";
    }
}

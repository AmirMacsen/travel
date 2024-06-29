package com.example.travel.controller.backstage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.pojo.Permission;
import com.example.travel.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/backstage/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @RequestMapping("/all")
    @PreAuthorize("hasAuthority('/permission/all')")
    public ModelAndView all(@RequestParam(value = "page", defaultValue = "1") int page,
                      @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Permission> selectPage = permissionService.findPage(1, 10);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("permissionPage", selectPage);
        modelAndView.setViewName("backstage/permission_all");
        return modelAndView;
    }

    @RequestMapping("/edit")
    @PreAuthorize("hasAuthority('/permission/edit')")
    public ModelAndView edit(int pid) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("permission", permissionService.findPermissionById(pid));
        modelAndView.setViewName("backstage/permission_edit");
        return modelAndView;
    }

    @RequestMapping("/update")
    @PreAuthorize("hasAuthority('/permission/update')")
    public String update(Permission permission) {
        permissionService.update(permission);
        return "redirect:/backstage/permission/all";
    }

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('/permission/add')")
    public String add(Permission permission) {
        permissionService.add(permission);
        return "redirect:/backstage/permission/all";
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('/permission/delete')")
    public String delete(int pid) {
        permissionService.delete(pid);
        return "redirect:/backstage/permission/all";
    }

}

package com.example.travel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.bean.RoleWithStatus;
import com.example.travel.mapper.AdminMapper;
import com.example.travel.mapper.RoleMapper;
import com.example.travel.pojo.Admin;
import com.example.travel.pojo.Permission;
import com.example.travel.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    AdminMapper adminMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<Admin> findPage(int page, int size) {
        Page seletedPage = adminMapper.selectPage(new Page(page, size), null);
        return seletedPage;
    }

    public void add(Admin admin){
        admin.setStatus(true);
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminMapper.insert(admin);
    }

    public Admin findAdminById(int id){
        Admin admin = adminMapper.selectById(id);
        return admin;
    }

    public int update(Admin admin){
        // 处理密码问题
        String oldPassword = adminMapper.selectById(admin.getAid()).getPassword();
        if (!oldPassword.equals(admin.getPassword())){
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        return adminMapper.updateById(admin);
    }


    public Admin findDesc(int id){
        return adminMapper.findDesc(id);
    }

    // 查询用户角色
    public List<RoleWithStatus> findRole(int id){
        // 1. 查询所有角色
        List<Role> roles = roleMapper.selectList(null);
        // 2. 查询用户具有的角色
        List<Integer> roleIds = roleMapper.findRoleIdByAdmin(id);
        // 3. 创建返回数据
        List<RoleWithStatus> roleWithStatuses = new ArrayList<>();
        for (Role role : roles) {
            RoleWithStatus roleWithStatus = new RoleWithStatus();
            roleWithStatus.setRid(role.getRid());
            roleWithStatus.setRoleName(role.getRoleName());
            roleWithStatus.setRoleDesc(role.getRoleDesc());
            roleWithStatus.setHasRole(roleIds.contains(role.getRid()));
            roleWithStatuses.add(roleWithStatus);
        }
        return roleWithStatuses;
    }

    // 更新用户角色
    public void updateRole(int aid, List<Integer> roleIds){
        transactionTemplate.execute(transactionStatus -> {
            // 1. 删除用户所有角色
            adminMapper.deleteAdminAllRole(aid);
            // 2. 添加用户角色
            for (Integer roleId : roleIds) {
                adminMapper.addAdminRole(aid, roleId);
            }
            return null;
        });
    }

    // 修改用户状态
    public void updateStatus(int id){
        Admin admin = adminMapper.selectById(id);
        admin.setStatus(!admin.isStatus()); // 状态取反
        adminMapper.updateById(admin);
    }

    // 根据用户名查询管理员
    public Admin findByUsername(String username){
        QueryWrapper<Admin> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("username", username);
        return adminMapper.selectOne(objectQueryWrapper);
    }

    // 根据用户名查询权限
    public List<Permission> findPermissionByUsername(String username){
        return adminMapper.findPermissionByUsername(username);
    }

}

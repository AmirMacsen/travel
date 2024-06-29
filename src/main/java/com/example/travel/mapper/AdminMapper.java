package com.example.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel.pojo.Admin;
import com.example.travel.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {
    // 查询用户详情
    Admin findDesc(int id);
    // 删除用户所有角色
    void deleteAdminAllRole(int aid);
    // 添加用户角色
    void addAdminRole(@Param("aid") int adminId, @Param("rid") int roleId);
    // 根据用户名查询权限
    List<Permission> findPermissionByUsername(String username);

}

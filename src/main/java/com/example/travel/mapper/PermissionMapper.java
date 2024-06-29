package com.example.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel.pojo.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    // 查询角色的权限Id
    List<Integer> findPermissionIdByRole(int rid);
    // 删除角色的所有权限
    void deletePermissionByRole(int rid);
    // 给角色添加权限
    void addPermissionByRole(@Param("rid") int rid, @Param("pid") int pid);
}

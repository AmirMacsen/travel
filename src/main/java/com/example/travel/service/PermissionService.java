package com.example.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.mapper.PermissionMapper;
import com.example.travel.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    PermissionMapper permissionMapper;

    // 查找Permission分页数据
    public Page<Permission> findPage(int page, int size){
        return permissionMapper.selectPage(new Page(page, size), null);
    }

    // 根据PermissioniD查找
    public Permission findPermissionById(int id){
        return permissionMapper.selectById(id);
    }
    // 更新Permission
    public int update(Permission permission){
        return permissionMapper.updateById(permission);
    }

    // 添加permission
    public void add(Permission permission){
        permissionMapper.insert(permission);
    }
    // 删除Permission
    public void delete(int pid){
        permissionMapper.deleteById(pid);
    }
}


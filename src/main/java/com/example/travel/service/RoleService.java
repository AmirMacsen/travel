package com.example.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.bean.PermissionWithStatus;
import com.example.travel.mapper.PermissionMapper;
import com.example.travel.mapper.RoleMapper;
import com.example.travel.pojo.Permission;
import com.example.travel.pojo.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private TransactionTemplate transactionTemplate;

    // 查询角色列表
    public Page<Role> findPage(int page, int size) {
        return roleMapper.selectPage(new Page(page, size), null);
    }

    // 添加角色
    public void add(Role role){
        roleMapper.insert(role);
    }

    // 根据ID查找角色
    public Role findRoleById(int id){
        return roleMapper.selectById(id);
    }

    // 修改角色
    public int update(Role role){
        return roleMapper.updateById(role);
    }

    // 删除角色
    public void delete(int id){
        roleMapper.deleteById(id);
    }

    // 查询角色拥有的权限集合
    public List<PermissionWithStatus> findPermission(int rid){
        // 1.查询所有的权限
        List<Permission> permissions = permissionMapper.selectList(null);

        // 2.查询角色拥有的权限ID
        List<Integer> permissionIds = permissionMapper.findPermissionIdByRole(rid);

        // 3.组合数据
        ArrayList<PermissionWithStatus> permissionWithStatuses = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionWithStatus permissionWithStatus = new PermissionWithStatus();
            BeanUtils.copyProperties(permission, permissionWithStatus);
            permissionWithStatus.setHasPermission(permissionIds.contains(permission.getPid()));
            permissionWithStatuses.add(permissionWithStatus);
        }
        return permissionWithStatuses;
    }

    // 更新角色权限
    public void updateRolePermission(int rid, List<Integer> permissionIds){
        transactionTemplate.execute(tx -> {
            permissionMapper.deletePermissionByRole(rid);
            permissionIds.forEach(permissionId -> {
                permissionMapper.addPermissionByRole(rid, permissionId);
            });
            return null;
        });
    }
}

package com.example.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel.pojo.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    // 根据用户查询角色id
    List<Integer> findRoleIdByAdmin(int  aid);

}

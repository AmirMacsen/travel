package com.example.travel.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class Role {
    @TableId
    private Integer rid;
    private String roleName;
    private String roleDesc;
    @TableField(exist = false)
    private List<Permission> permissionList;
}

package com.example.travel.bean;

import lombok.Data;

/**
 * 带有用户状态的role
 */
@Data
public class RoleWithStatus {
    private int rid;
    private String roleName;
    private String roleDesc;
    private Boolean hasRole;
}

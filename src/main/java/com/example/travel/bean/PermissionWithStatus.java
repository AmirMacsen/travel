package com.example.travel.bean;

import lombok.Data;

// 带状态的权限实体类
@Data
public class PermissionWithStatus {
    private int pid;
    private String permissionName;
    private String permissionDesc;
    private Boolean hasPermission;
}

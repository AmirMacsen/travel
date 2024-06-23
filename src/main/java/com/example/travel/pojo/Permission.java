package com.example.travel.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 权限相关
 */
@Data
public class Permission {
    @TableId
    private Integer pid;
    private String permissionName;
    private String permissionDesc;
}

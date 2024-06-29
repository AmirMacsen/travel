package com.example.travel.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class Admin {
    @TableId
    private Integer aid;
    private String username;
    private String password;
    private String email;
    private String phoneNum;
    private boolean status;
    @TableField(exist = false)
    private List<Role> roleList;
}

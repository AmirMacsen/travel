package com.example.travel.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    @TableId
    private Integer pid; // 产品ID
    private String productName; // 产品名称
    private BigDecimal price; // 单价
    private String hotline; // 热线电话
    private Boolean status; // 状态 0 开启 1 关闭
    private String productDesc; // 产品描述
    private String pImage; // 产品图片
    private Integer cid; // 产品分类id
    @TableField(exist = false) // 表明不是数据库的实体属性，而是映射的
    private Category category; // 产品分类
}

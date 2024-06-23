package com.example.travel.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 产品类型
 */
@Data
public class Category {
    @TableId
    private Integer cid;
    private String cname;

}

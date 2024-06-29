package com.example.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.pojo.Product;
import org.apache.ibatis.annotations.Param;

public interface ProductMapper extends BaseMapper<Product> {
    // 分页查询产品信息
    Page<Product> findProductPage(Page<Product> page);

    // 根据Id查询产品
    Product findById(Integer pid);

    int findFavoritePidAndMid(@Param("pid")Integer pid,@Param("mid")Integer mid);

    void addFavorite(@Param("pid")Integer pid,@Param("mid")Integer mid);
    void delFavorite(@Param("pid")Integer pid,@Param("mid")Integer mid);

    Page<Product> findMemberFavorite(Page<Product> page,Integer mid);
}

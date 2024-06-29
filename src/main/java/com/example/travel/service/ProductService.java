package com.example.travel.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.mapper.ProductMapper;
import com.example.travel.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ProductService {
    @Autowired
    ProductMapper productMapper;

    public Page<Product> findProductPage(int page, int size){
        return productMapper.findProductPage(new Page<>(page, size));
    }

    public void add(Product product){
        productMapper.insert(product);
    }

    public Product findById(Integer pid){
        return productMapper.findById(pid);
    }

    public void update(Product product){
        Product oldProduct = productMapper.findById(product.getPid());
        if(product.getPImage().isEmpty()) {
            product.setPImage(oldProduct.getPImage());
        }
        productMapper.updateById(product);
    }

    public void updateStatus(Integer pid){
        Product product = productMapper.selectById(pid);
        product.setStatus(!product.getStatus());
        productMapper.updateById(product);
    }

    public Page<Product> findProduct(Integer cid,String productName,int page,int size){
        QueryWrapper<Product> queryWrapper = new QueryWrapper();
        if (cid != null){
            queryWrapper.eq("cid",cid);
        }
        if (StringUtils.hasText(productName)){
            queryWrapper.like("productName",productName);
        }
        // 还在启动的旅游产品
        queryWrapper.eq("status",1);
        // 倒序排列
        queryWrapper.orderByDesc("pid");

        Page selectPage = productMapper.selectPage(new Page(page, size), queryWrapper);
        return selectPage;
    }
}


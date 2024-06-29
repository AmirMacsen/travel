package com.example.travel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.mapper.CategoryMapper;
import com.example.travel.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    // 分页查找
    public Page<Category> findPage(int page, int size) {
        Page<Category> pageInfo = new Page<>(page, size);
        categoryMapper.selectPage(pageInfo, null);
        return pageInfo;
    }

    // 添加类型
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    public Category findById(Integer cid){
        return categoryMapper.selectById(cid);
    }

    public void update(Category category){
        categoryMapper.updateById(category);
    }

    public void delete(Integer cid){
        categoryMapper.deleteById(cid);
    }

    // 查询所有类型
    public List<Category> findAll() {
        return categoryMapper.selectList(null);
    }
}

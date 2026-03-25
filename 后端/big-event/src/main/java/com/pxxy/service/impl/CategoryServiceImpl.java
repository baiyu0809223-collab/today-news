package com.pxxy.service.impl;

import com.pxxy.mapper.CategoryMapper;
import com.pxxy.pojo.Category;
import com.pxxy.service.CategoryService;
import com.pxxy.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(Category category) {
        //补充属性值
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map=ThreadLocalUtil.get();
        category.setCreateUser((Integer)map.get("id"));

        categoryMapper.add(category);
    }

    @Override
    public boolean findByName(String categoryName) {
        if (categoryMapper.findByName(categoryName)!=null){
            return true;
        }else return false;
    }

    @Override
    public List<Category> list() {
        Map <String,Object> map= ThreadLocalUtil.get();

        return categoryMapper.list((Integer) map.get("id"));
    }

    @Override
    public Category findById(Integer id) {
        Category category=categoryMapper.findById(id);
        return category;
    }

    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);
    }

    @Override
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    @Override
    public List<Category> Alllist() {
        return categoryMapper.Alllist();
    }
}

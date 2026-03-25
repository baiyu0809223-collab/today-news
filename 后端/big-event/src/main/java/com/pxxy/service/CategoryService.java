package com.pxxy.service;


import com.pxxy.pojo.Category;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public interface CategoryService {

    //新增分类
    public void add(Category category);

    //列表查询
    List<Category> list();

    //通过名字查询
    public boolean findByName(@NotEmpty String categoryName);

    //根据Id查询分类信息
    Category findById(Integer id);

    //修改分类信息
    void update(Category category);

    //删除该分类
    void delete(Integer id);

    //获取所有分类信息
    List<Category> Alllist();
}

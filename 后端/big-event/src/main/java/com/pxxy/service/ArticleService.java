package com.pxxy.service;

import com.pxxy.pojo.Article;
import com.pxxy.pojo.Category;
import com.pxxy.pojo.PageBean;

import java.util.List;

public interface ArticleService {

    //添加文章
    public void add(Article article);

    //条件分页列表查询
    public PageBean<Article> list(Integer page, Integer size, String categoryId, String state,String title);

    //条件分页所有列表查询
    public PageBean<Article> alllist(Integer page, Integer size, String categoryId,String createUser, String state);

    //查看文章的详情
    public Article findById(Integer id);

    //更新文章
    void update(Article article);

    //删除文章
    void delete(Integer id);

    //获取所有文章
    List<Article> list(Integer categoryId,String keyword);
    //草稿删除
    void draftDelete(Integer id);

    //草稿修改
    void draftUpdata(Article article);



}

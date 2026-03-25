package com.pxxy.service;

import com.pxxy.pojo.Article;
import com.pxxy.pojo.Examine;
import com.pxxy.pojo.PageBean;

public interface ExamineService {

    //添加文章
    public void add(Examine examine);

    //删除文章
    void delete(Integer id, Integer aid);

    //条件分页列表查询
    public PageBean<Examine> list(Integer page, Integer size, String categoryId,String createUser, String state);

    //审核文章删除
    void articleDelete(Integer id,Integer aid);

    //更新文章
    void update(Examine examine);
}

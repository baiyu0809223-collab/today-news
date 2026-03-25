package com.pxxy.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pxxy.mapper.ArticleMapper;
import com.pxxy.mapper.ExamineMapper;
import com.pxxy.pojo.Article;
import com.pxxy.pojo.Examine;
import com.pxxy.pojo.PageBean;
import com.pxxy.service.ExamineService;
import com.pxxy.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ExamineServicelmpl implements ExamineService {

    @Autowired
    private ExamineMapper examineMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Examine examine) {
        examine.setCreateTime(LocalDateTime.now());
        examine.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map = ThreadLocalUtil.get();
        examine.setCreateUser((Integer) map.get("id"));
        examine.setTarget("发布");

        examineMapper.add(examine);
    }

    //审核不通过
    @Override
    public void delete(Integer id ,Integer aid) {
        articleMapper.stateUpdate(aid);
        examineMapper.delete(id);
    }

    @Override
    public PageBean<Examine> list(Integer page, Integer size, String categoryId,String createUser, String state) {
        //创建PageBean对象
        PageBean<Examine> pageBean = new PageBean<>();

        //开启分页查询 pageHelper
        PageHelper.startPage(page,size);

        // 调用mapper，只使用categoryId和state进行查询
        List<Examine> examines = examineMapper.list(categoryId,createUser, state);
        //Page中提供了方法，可以获取PageHelper分页查询后得到的总条数和当前页数据
        Page<Examine> p= (Page<Examine>) examines;

        //把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    @Override
    public void articleDelete(Integer id, Integer aid) {
        examineMapper.delete(id);
        articleMapper.delete(aid);
    }

    @Override
    public void update(Examine examine) {
        examine.setUpdateTime(LocalDateTime.now());
        Article article = new Article();
        BeanUtils.copyProperties(examine, article, "id"); // 复制属性，排除 id
        article.setId(examine.getAid());
        articleMapper.update(article);
        examineMapper.delete(examine.getId());
    }
}

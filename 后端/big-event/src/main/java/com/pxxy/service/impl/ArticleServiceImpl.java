package com.pxxy.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pxxy.mapper.ArticleMapper;
import com.pxxy.mapper.ExamineMapper;
import com.pxxy.pojo.Article;
import com.pxxy.pojo.Examine;
import com.pxxy.pojo.PageBean;
import com.pxxy.service.ArticleService;
import com.pxxy.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ExamineMapper examineMapper;

    @Override
    public void add(Article article) {
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        if(article.getManage() != null){
            Map<String,Object> map = ThreadLocalUtil.get();
            article.setCreateUser((Integer) map.get("id"));
            articleMapper.add(article);
        }else{
            examineMapper.delete(article.getId());
            articleMapper.add(article);
        }
    }

    @Override
    public PageBean<Article> list(Integer page, Integer size, String categoryId, String state,String title) {
        //创建PageBean对象
        PageBean<Article> pageBean = new PageBean<>();

        //开启分页查询 pageHelper
        PageHelper.startPage(page,size);

        //调用mapper
        Integer  create_user=null;
        Map<String,Object> map = ThreadLocalUtil.get();
        List<Article>as=null;
        if (title!=null){
            as=articleMapper.listbykey((Integer)map.get("id"),categoryId,state,title);
        }else as= articleMapper.list((Integer)map.get("id"),categoryId,state);
        //Page中提供了方法，可以获取PageHelper分页查询后得到的总条数和当前页数据
        Page<Article> p= (Page<Article>) as;

        //把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    @Override
    public PageBean<Article> alllist(Integer page, Integer size, String categoryId,String createUser, String state) {
        // 创建PageBean对象
        PageBean<Article> pageBean = new PageBean<>();

        // 开启分页查询
        PageHelper.startPage(page, size);

        // 调用mapper，只使用categoryId和state进行查询
        List<Article> articles = articleMapper.alllist(categoryId,createUser, state);

        // 将List转换为Page对象
        Page<Article> p = (Page<Article>) articles;

        // 把数据填充到PageBean对象中
        pageBean.setTotal(p.getTotal());
        pageBean.setItems(p.getResult());
        return pageBean;
    }

    @Override
    public Article findById(Integer id) {
        Article article=articleMapper.findById(id);
        article.setViews(article.getViews()+1);
        articleMapper.addOneViews(article);
        return article;
    }

    @Override
    public void update(Article article) {

        article.setCreateTime(LocalDateTime.now());
        Examine examine = new Examine();
        BeanUtils.copyProperties(article, examine, "id"); // 复制属性，排除 id
        examine.setAid(article.getId()); // 单独设置 aid
        examine.setTarget("更新");
        examineMapper.add(examine);
    }

    @Override
    public void delete(Integer id) {
        Article article = articleMapper.findById(id);
        Examine examine = new Examine();
        BeanUtils.copyProperties(article, examine, "id"); // 复制属性，排除 id
        examine.setAid(article.getId()); // 单独设置 aid
        examine.setTarget("删除");
        examineMapper.add(examine);
    }

    @Override
    public List<Article> list(Integer categoryId, String keyword) {
        return articleMapper.getAll(categoryId,keyword);
    }

    //删除草稿
    @Override
    public void draftDelete(Integer id) {
        articleMapper.delete(id);
    }

    //修改草稿
    @Override
    public void draftUpdata(Article article) {
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article);
    }
}

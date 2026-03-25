package com.pxxy.mapper;

import com.pxxy.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {


    //添加文章
    @Insert("insert into article(title, content, category_id, create_user, create_time, update_time, cover_img, state) " +
            "values (#{title},#{content},#{categoryId},#{createUser},#{createTime},#{updateTime},#{coverImg},#{state})")
    void add(Article article);

    //查询列表
    List<Article> list(Integer id, String categoryId, String state);

    //查询所有用户发表的书的列表
    List<Article> alllist(String categoryId,String createUser, String state);

    //通过id查询文章
    @Select("select *from article where id=#{id}")
    Article findById(Integer id);

    //修改文章信息
    @Update("update article set title=#{title},content=#{content},category_id=#{categoryId},update_time=#{updateTime},cover_img=#{coverImg},state=#{state} where id=#{id}")
    void update(Article article);

    //删除文章
    @Delete("delete from article where id=#{id}")
    void delete(Integer id);

    //获取所有文章
    List<Article> getAll(Integer id,String keyword);
    //修改书籍状态
    @Update("UPDATE article SET state = '已发布' WHERE id = #{aid}")
    void stateUpdate(Integer aid);

    //浏览量加1
    @Update("update article set views=#{views} where id=#{id}")
    void addOneViews(Article article);

    //搜索
    List<Article> listbykey(Integer id, String categoryId, String state, String title);
}

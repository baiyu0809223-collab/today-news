package com.pxxy.mapper;

import com.pxxy.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("INSERT INTO comment(article_id, user_id, username, user_pic, content, parent_id, create_time, update_time, state, like_count, is_top) " +
            "VALUES(#{articleId}, #{userId}, #{username}, #{userPic}, #{content}, #{parentId}, #{createTime}, #{updateTime}, #{state}, #{likeCount}, #{isTop})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Comment comment);

    @Update("UPDATE comment SET state = #{status} WHERE id = #{commentId}")
    void updateStatus(@Param("commentId") Integer commentId, @Param("status") String status);

    @Select("SELECT * FROM comment WHERE id = #{id}")
    Comment selectById(Integer id);

    @Select("SELECT * FROM comment WHERE article_id = #{articleId} AND state = 'APPROVED' AND parent_id IS NULL " +
            "ORDER BY ${orderBy}")
    List<Comment> selectByArticleId(@Param("articleId") Integer articleId, @Param("orderBy") String orderBy);

    @Update("UPDATE comment SET like_count = like_count + 1 WHERE id = #{commentId}")
    void increaseLikeCount(Integer commentId);

    @Update("UPDATE comment SET like_count = like_count - 1 WHERE id = #{commentId}")
    void decreaseLikeCount(Integer commentId);

    @Select("SELECT * FROM comment WHERE parent_id = #{parentId} AND state = 'APPROVED'")
    List<Comment> selectRepliesByParentId(@Param("parentId") Integer parentId);

    @Update("UPDATE comment SET like_count = #{count} WHERE id = #{commentId}")
    void updateLikeCount(@Param("commentId") Integer commentId, @Param("count") Integer count);
}
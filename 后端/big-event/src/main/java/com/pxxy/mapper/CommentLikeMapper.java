package com.pxxy.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface CommentLikeMapper {
    @Insert("INSERT INTO comment_like(comment_id, user_id) VALUES(#{comment_id}, #{user_id})")
    int insert(@Param("comment_id") Integer commentId, @Param("user_id") Integer userId);

    @Delete("DELETE FROM comment_like WHERE comment_id = #{comment_id} AND user_id = #{user_id}")
    int delete(@Param("comment_id") Integer commentId, @Param("user_id") Integer userId);

    @Select("SELECT COUNT(*) > 0 FROM comment_like WHERE comment_id = #{comment_id} AND user_id = #{user_id}")
    boolean exists(@Param("comment_id") Integer commentId, @Param("user_id") Integer userId);

    @Select("SELECT COUNT(*) FROM comment_like WHERE comment_id = #{comment_id}")
    int countByCommentId(@Param("comment_id") Integer commentId);

    @Delete("DELETE FROM comment_like WHERE comment_id = #{comment_id}")
    int deleteByCommentId(@Param("comment_id") Integer commentId);
}
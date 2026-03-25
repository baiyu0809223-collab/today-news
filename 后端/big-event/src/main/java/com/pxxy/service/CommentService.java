package com.pxxy.service;

import com.pxxy.pojo.Comment;
import com.pxxy.pojo.PageBean;
import java.util.List;

public interface CommentService {
    void add(Comment comment);
    void delete(Integer commentId, Integer user_id, boolean isAdmin);
    PageBean<Comment> getCommentsByArticle(Integer article_id, Integer page, Integer pageSize, String sortType);
    void toggleLike(Integer comment_id, Integer user_id);
    boolean isLiked(Integer comment_id, Integer user_id);
    List<Comment> getReplies(Integer parent_id);
    void updateStatus(Integer comment_id, String status);
    int getLikeCount(Integer comment_id);
}
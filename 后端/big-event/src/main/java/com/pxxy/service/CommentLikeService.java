package com.pxxy.service;

public interface CommentLikeService {
    void toggleLike(Integer comment_id, Integer user_id);
    boolean isLiked(Integer comment_id, Integer user_id);
    int getLikeCount(Integer comment_id);
    void deleteByCommentId(Integer comment_id);
}
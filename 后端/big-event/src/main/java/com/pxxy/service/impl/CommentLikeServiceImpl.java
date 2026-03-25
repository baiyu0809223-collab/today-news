package com.pxxy.service.impl;

import com.pxxy.mapper.CommentLikeMapper;
import com.pxxy.mapper.CommentMapper;
import com.pxxy.service.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    @Autowired
    private CommentLikeMapper commentLikeMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    @Transactional
    public void toggleLike(Integer comment_id, Integer user_id) {
        boolean isLiked = commentLikeMapper.exists(comment_id, user_id);
        if (isLiked) {
            commentLikeMapper.delete(comment_id, user_id);
            commentMapper.decreaseLikeCount(comment_id);
        } else {
            commentLikeMapper.insert(comment_id, user_id);
            commentMapper.increaseLikeCount(comment_id);
        }
    }

    @Override
    public boolean isLiked(Integer comment_id, Integer user_id) {
        return commentLikeMapper.exists(comment_id, user_id);
    }

    @Override
    public int getLikeCount(Integer comment_id) {
        return commentLikeMapper.countByCommentId(comment_id);
    }

    @Override
    @Transactional
    public void deleteByCommentId(Integer comment_id) {
        commentLikeMapper.deleteByCommentId(comment_id);
    }
}
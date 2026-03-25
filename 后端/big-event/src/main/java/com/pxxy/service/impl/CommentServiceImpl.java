package com.pxxy.service.impl;

import com.github.pagehelper.PageHelper;
import com.pxxy.mapper.CommentMapper;
import com.pxxy.pojo.Comment;
import com.pxxy.pojo.PageBean;
import com.pxxy.pojo.User;
import com.pxxy.service.CommentService;
import com.pxxy.service.CommentLikeService;
import com.pxxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentLikeService commentLikeService;
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void add(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        comment.setState("APPROVED");
        comment.setLikeCount(0);
        comment.setIsTop(false);
        commentMapper.insert(comment);
    }

    @Override
    @Transactional
    public void delete(Integer comment_id, Integer user_id, boolean isAdmin) {
        Comment comment = commentMapper.selectById(comment_id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        if (!isAdmin && (comment.getUserId() == null || !comment.getUserId().equals(user_id))) {
            throw new RuntimeException("无权删除该评论");
        }
//        if (!isAdmin && !comment.getUser_id().equals(user_id)) {
//            throw new RuntimeException("无权删除该评论");
//        }

        commentLikeService.deleteByCommentId(comment_id);
        commentMapper.updateStatus(comment_id, "DELETED");
    }

    @Override
    public PageBean<Comment> getCommentsByArticle(Integer article_id, Integer page, Integer pageSize, String sortType) {
        String orderBy = "create_time DESC";
        if ("like".equals(sortType)) {
            orderBy = "like_count DESC, create_time DESC";
        }

        PageHelper.startPage(page, pageSize);
        List<Comment> comments = commentMapper.selectByArticleId(article_id, orderBy);

        for (Comment comment : comments) {
            List<Comment> replies = commentMapper.selectRepliesByParentId(comment.getId());
            User user=userService.findUserById(comment.getUserId());
            comment.setUsername(user.getNickname());
            comment.setUserPic(user.getUserPic());
            comment.setReplies(replies);
        }

        long total = ((com.github.pagehelper.Page<?>) comments).getTotal();
        return new PageBean<>(total, comments);
    }

    @Override
    @Transactional
    public void toggleLike(Integer comment_id, Integer user_id) {
        commentLikeService.toggleLike(comment_id, user_id);
    }

    @Override
    public boolean isLiked(Integer comment_id, Integer user_id) {
        return commentLikeService.isLiked(comment_id, user_id);
    }

    @Override
    public int getLikeCount(Integer comment_id) {
        return commentLikeService.getLikeCount(comment_id);
    }

    @Override
    public List<Comment> getReplies(Integer parent_id) {
        List<Comment> comments=commentMapper.selectRepliesByParentId(parent_id);
        for (Comment comment : comments) {
            User u=userService.findUserById(comment.getUserId());
            comment.setUserPic(u.getUserPic());
            comment.setUsername(u.getNickname());

        }
        return comments;
    }

    @Override
    @Transactional
    public void updateStatus(Integer comment_id, String status) {
        commentMapper.updateStatus(comment_id, status);
    }
}
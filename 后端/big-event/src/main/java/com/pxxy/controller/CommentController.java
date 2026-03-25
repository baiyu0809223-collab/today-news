package com.pxxy.controller;

import com.pxxy.pojo.Comment;
import com.pxxy.pojo.PageBean;
import com.pxxy.pojo.Result;
import com.pxxy.pojo.User;
import com.pxxy.service.CommentService;
import com.pxxy.service.UserService;
import com.pxxy.utils.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "评论管理", description = "文章评论、回复、点赞、状态管理相关接口")
@RestController
@RequestMapping("/comment")
@Validated
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    // =========================
    // 添加评论
    // =========================
    @Operation(
            summary = "添加评论",
            description = "对文章或评论添加评论（支持一级评论和回复）"
    )
    @PostMapping
    public Result add(
            @Parameter(description = "评论对象", required = true)
            @RequestBody Comment comment) {

        Map<String, Object> user = ThreadLocalUtil.get();
        comment.setUserId((Integer) user.get("id"));
        comment.setUsername((String) user.get("nickname"));

        User u = userService.findUserById((Integer) user.get("id"));
        comment.setUserPic(u.getUserPic());

        commentService.add(comment);
        return Result.success();
    }

    // =========================
    // 根据文章分页查询评论
    // =========================
    @Operation(
            summary = "分页查询文章评论",
            description = "根据文章ID分页查询评论列表，支持排序方式"
    )
    @GetMapping("/article/{article_id}")
    public Result<PageBean<Comment>> listByArticle(
            @Parameter(description = "文章ID", required = true, example = "1")
            @PathVariable @Min(1) Integer article_id,

            @Parameter(description = "页码", example = "1")
            @RequestParam(defaultValue = "1") @Min(1) Integer page,

            @Parameter(description = "每页条数", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) Integer pageSize,

            @Parameter(description = "排序方式（time / like）", example = "time")
            @RequestParam(defaultValue = "time") String sortType) {

        PageBean<Comment> comments = commentService.getCommentsByArticle(
                article_id, page, pageSize, sortType);

        return Result.success(comments);
    }

    // =========================
    // 删除评论
    // =========================
    @Operation(
            summary = "删除评论",
            description = "用户删除自己的评论，管理员可删除任意评论"
    )
    @DeleteMapping("/{id}")
    public Result delete(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer id) {

        Map<String, Object> user = ThreadLocalUtil.get();
        Integer user_id = (Integer) user.get("id");
        Integer role = (Integer) user.get("role");

        if (role == null) {
            User dbUser = userService.findUserById(user_id);
            role = dbUser.getRole();
        }

        boolean isAdmin = (role != null && role == 1);

        try {
            commentService.delete(id, user_id, isAdmin);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    // =========================
    // 评论点赞 / 取消点赞
    // =========================
    @Operation(
            summary = "评论点赞/取消点赞",
            description = "对评论进行点赞或取消点赞（自动切换）"
    )
    @PostMapping("/{comment_id}/like")
    public Result toggleLike(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer comment_id) {

        Map<String, Object> user = ThreadLocalUtil.get();
        commentService.toggleLike(comment_id, (Integer) user.get("id"));
        return Result.success();
    }

    // =========================
    // 查询是否已点赞
    // =========================
    @Operation(
            summary = "查询是否已点赞",
            description = "查询当前用户是否对该评论点过赞"
    )
    @GetMapping("/{comment_id}/isLiked")
    public Result<Boolean> isLiked(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer comment_id) {

        Map<String, Object> user = ThreadLocalUtil.get();
        return Result.success(
                commentService.isLiked(comment_id, (Integer) user.get("id"))
        );
    }

    // =========================
    // 获取点赞数量
    // =========================
    @Operation(
            summary = "获取评论点赞数量",
            description = "获取指定评论的点赞总数"
    )
    @GetMapping("/{comment_id}/likes/count")
    public Result<Integer> getLikeCount(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer comment_id) {

        return Result.success(commentService.getLikeCount(comment_id));
    }

    // =========================
    // 获取评论回复
    // =========================
    @Operation(
            summary = "获取评论回复",
            description = "根据父评论ID获取所有回复"
    )
    @GetMapping("/{parent_id}/replies")
    public Result<List<Comment>> getReplies(
            @Parameter(description = "父评论ID", required = true, example = "50")
            @PathVariable Integer parent_id) {

        return Result.success(commentService.getReplies(parent_id));
    }

    // =========================
    // 更新评论状态
    // =========================
    @Operation(
            summary = "更新评论状态",
            description = "修改评论状态（如 APPROVED / REJECTED / DELETED）"
    )
    @PutMapping("/{comment_id}/status")
    public Result updateStatus(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer comment_id,

            @Parameter(description = "评论状态", required = true, example = "APPROVED")
            @RequestParam String status) {

        commentService.updateStatus(comment_id, status);
        return Result.success();
    }
}
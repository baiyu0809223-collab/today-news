package com.pxxy.controller;

import com.pxxy.pojo.Result;
import com.pxxy.service.CommentLikeService;
import com.pxxy.utils.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "评论点赞管理", description = "评论点赞、取消点赞、点赞状态与数量查询接口")
@RestController
@RequestMapping("/comment-like")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    // =========================
    // 点赞 / 取消点赞
    // =========================
    @Operation(
            summary = "评论点赞/取消点赞",
            description = "对指定评论进行点赞或取消点赞（同一接口自动切换状态）"
    )
    @PostMapping("/{comment_id}")
    public Result toggleLike(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer comment_id) {

        Map<String, Object> user = ThreadLocalUtil.get();
        commentLikeService.toggleLike(comment_id, (Integer) user.get("id"));
        return Result.success();
    }

    // =========================
    // 查询当前用户是否点赞
    // =========================
    @Operation(
            summary = "查询是否已点赞",
            description = "查询当前登录用户是否已对该评论点赞"
    )
    @GetMapping("/{comment_id}/status")
    public Result<Boolean> isLiked(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer comment_id) {

        Map<String, Object> user = ThreadLocalUtil.get();
        return Result.success(
                commentLikeService.isLiked(comment_id, (Integer) user.get("id"))
        );
    }

    // =========================
    // 查询评论点赞数
    // =========================
    @Operation(
            summary = "获取评论点赞数量",
            description = "获取指定评论的点赞总数"
    )
    @GetMapping("/{comment_id}/count")
    public Result<Integer> getLikeCount(
            @Parameter(description = "评论ID", required = true, example = "100")
            @PathVariable Integer comment_id) {

        return Result.success(commentLikeService.getLikeCount(comment_id));
    }
}
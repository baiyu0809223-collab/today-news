package com.pxxy.controller;

import com.pxxy.pojo.Article;
import com.pxxy.pojo.PageBean;
import com.pxxy.pojo.Result;
import com.pxxy.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "文章管理", description = "文章增删改查相关接口")
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    // =========================
    // 新增文章
    // =========================
    @Operation(
            summary = "新增文章",
            description = "创建一篇新的文章（草稿或发布）",
            responses = {
                    @ApiResponse(responseCode = "200", description = "创建成功")
            }
    )
    @PostMapping
    public Result add(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "文章对象",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Article.class))
            )
            @RequestBody Article article) {

        articleService.add(article);
        return Result.success();
    }

    // =========================
    // 后台文章分页列表
    // =========================
    @Operation(summary = "后台文章分页查询")
    @GetMapping("/alllist")
    public Result<PageBean<Article>> alllist(
            @Parameter(description = "页码", example = "1")
            Integer page,

            @Parameter(description = "每页条数", example = "10")
            Integer size,

            @Parameter(description = "分类ID", example = "1")
            @RequestParam(required = false) String categoryId,

            @Parameter(description = "创建用户ID", example = "1")
            @RequestParam(required = false) String createUser,

            @Parameter(description = "文章标题关键字", example = "Spring")
            @RequestParam(required = false) String title,

            @Parameter(description = "文章状态", example = "已发布")
            @RequestParam(required = false) String state) {

        PageBean<Article> pb = articleService.alllist(page, size, categoryId, createUser, state);
        return Result.success(pb);
    }

    // =========================
    // 前台文章分页列表
    // =========================
    @Operation(summary = "文章分页查询")
    @GetMapping
    public Result<PageBean<Article>> list(
            @Parameter(description = "页码", example = "1")
            Integer page,

            @Parameter(description = "每页条数", example = "10")
            Integer size,

            @Parameter(description = "分类ID", example = "1")
            @RequestParam(required = false) String categoryId,

            @Parameter(description = "文章标题关键字", example = "Java")
            @RequestParam(required = false) String title,

            @Parameter(description = "文章状态", example = "已发布")
            @RequestParam(required = false) String state) {

        PageBean<Article> pb = articleService.list(page, size, categoryId, state,title);
        return Result.success(pb);
    }

    // =========================
    // 文章详情
    // =========================
    @Operation(summary = "获取文章详情")
    @GetMapping("/detail")
    public Result<Article> detail(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Integer id) {

        Article article = articleService.findById(id);
        return Result.success(article);
    }

    // =========================
    // 更新文章
    // =========================
    @Operation(summary = "更新文章")
    @PutMapping
    public Result update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "文章对象",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Article.class))
            )
            @RequestBody Article article) {

        articleService.update(article);
        return Result.success();
    }

    // =========================
    // 删除文章
    // =========================
    @Operation(summary = "删除文章")
    @DeleteMapping
    public Result delete(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Integer id) {

        articleService.delete(id);
        return Result.success();
    }

    // =========================
    // 查询所有文章（不分页）
    // =========================
    @Operation(summary = "查询所有文章")
    @GetMapping("/all")
    public Result<List<Article>> all(
            @Parameter(description = "分类ID", example = "1")
            @RequestParam(required = false) Integer categoryId,

            @Parameter(description = "关键字", example = "Spring")
            @RequestParam(required = false) String keyword) {

        List<Article> articles = articleService.list(categoryId, keyword);
        return Result.success(articles);
    }

    // =========================
    // 删除草稿
    // =========================
    @Operation(summary = "删除草稿")
    @DeleteMapping("/draftDelete")
    public Result draftDelete(
            @Parameter(description = "文章ID", required = true, example = "1")
            @RequestParam Integer id) {

        articleService.draftDelete(id);
        return Result.success();
    }

    // =========================
    // 修改草稿
    // =========================
    @Operation(summary = "修改草稿")
    @PutMapping("/draftUpdata")
    public Result draftUpdata(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "草稿文章对象",
                    required = true,
                    content = @Content(schema = @Schema(implementation = Article.class))
            )
            @RequestBody Article article) {

        articleService.draftUpdata(article);
        return Result.success();
    }
}
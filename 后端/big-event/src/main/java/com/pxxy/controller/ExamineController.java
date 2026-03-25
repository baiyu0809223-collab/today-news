package com.pxxy.controller;

import com.pxxy.pojo.Examine;
import com.pxxy.pojo.PageBean;
import com.pxxy.pojo.Result;
import com.pxxy.service.ExamineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "文章审核管理", description = "文章审核、新增、删除、查询、更新相关接口")
@RestController
@RequestMapping("/examine")
public class ExamineController {

    @Autowired
    private ExamineService examineService;

    // =========================
    // 新增审核文章
    // =========================
    @Operation(
            summary = "新增文章审核",
            description = "提交文章审核信息"
    )
    @PostMapping
    public Result add(
            @Parameter(description = "审核文章对象", required = true)
            @RequestBody Examine examine) {

        examineService.add(examine);
        return Result.success();
    }

    // =========================
    // 审核失败（删除审核记录）
    // =========================
    @Operation(
            summary = "审核失败",
            description = "审核失败时删除审核记录，并处理对应文章"
    )
    @DeleteMapping
    public Result delete(
            @Parameter(description = "审核记录ID", required = true, example = "1")
            @RequestParam Integer id,

            @Parameter(description = "文章ID", required = true, example = "10")
            Integer aid) {

        examineService.delete(id, aid);
        return Result.success();
    }

    // =========================
    // 审核列表（分页）
    // =========================
    @Operation(
            summary = "审核列表查询",
            description = "分页查询文章审核列表，可按条件筛选"
    )
    @GetMapping
    public Result<PageBean<Examine>> list(
            @Parameter(description = "页码", example = "1")
            Integer page,

            @Parameter(description = "每页条数", example = "10")
            Integer size,

            @Parameter(description = "分类ID", example = "1")
            @RequestParam(required = false) String categoryId,

            @Parameter(description = "创建用户ID", example = "1")
            @RequestParam(required = false) String createUser,

            @Parameter(description = "审核状态", example = "PENDING")
            @RequestParam(required = false) String state) {

        PageBean<Examine> pb = examineService.list(page, size, categoryId, createUser, state);
        return Result.success(pb);
    }

    // =========================
    // 删除文章审核（通过审核后）
    // =========================
    @Operation(
            summary = "删除文章审核",
            description = "文章审核通过后，删除对应审核记录"
    )
    @DeleteMapping("/articleDelete")
    public Result articleDelete(
            @Parameter(description = "审核记录ID", required = true, example = "1")
            Integer id,

            @Parameter(description = "文章ID", required = true, example = "10")
            Integer aid) {

        examineService.articleDelete(id, aid);
        return Result.success();
    }

    // =========================
    // 更新审核信息
    // =========================
    @Operation(
            summary = "更新文章审核信息",
            description = "更新审核内容或状态"
    )
    @PutMapping
    public Result update(
            @Parameter(description = "审核文章对象", required = true)
            @RequestBody Examine examine) {

        examineService.update(examine);
        return Result.success();
    }
}
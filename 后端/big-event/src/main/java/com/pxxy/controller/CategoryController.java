package com.pxxy.controller;

import com.pxxy.pojo.Category;
import com.pxxy.pojo.Result;
import com.pxxy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "分类管理", description = "文章分类的新增、查询、修改、删除接口")
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // =========================
    // 添加分类
    // =========================
    @Operation(
            summary = "新增分类",
            description = "新增文章分类，分类名称不能重复"
    )
    @PostMapping
    public Result add(
            @Parameter(description = "分类对象（新增校验）", required = true)
            @RequestBody @Validated(Category.Add.class) Category category) {

        boolean find = categoryService.findByName(category.getCategoryName());
        if (find) {
            return Result.error("该分类已经存在");
        }
        categoryService.add(category);
        return Result.success();
    }

    // =========================
    // 查询当前用户的所有分类
    // =========================
    @Operation(
            summary = "查询分类列表",
            description = "查询当前用户创建的所有分类"
    )
    @GetMapping
    public Result<List<Category>> getAll() {

        List<Category> cs = categoryService.list();
        return Result.success(cs);
    }

    // =========================
    // 获取分类详情
    // =========================
    @Operation(
            summary = "获取分类详情",
            description = "根据分类ID获取分类详细信息"
    )
    @GetMapping("/detail")
    public Result<Category> detail(
            @Parameter(description = "分类ID", required = true, example = "1")
            Integer id) {

        Category category = categoryService.findById(id);
        return Result.success(category);
    }

    // =========================
    // 修改分类信息
    // =========================
    @Operation(
            summary = "修改分类信息",
            description = "根据分类ID修改分类名称或别名"
    )
    @PutMapping
    public Result update(
            @Parameter(description = "分类对象（更新校验）", required = true)
            @RequestBody @Validated(Category.Update.class) Category category) {

        categoryService.update(category);
        return Result.success();
    }

    // =========================
    // 删除分类
    // =========================
    @Operation(
            summary = "删除分类",
            description = "根据分类ID删除分类"
    )
    @DeleteMapping
    public Result delete(
            @Parameter(description = "分类ID", required = true, example = "1")
            Integer id) {

        categoryService.delete(id);
        return Result.success();
    }

    // =========================
    // 查询所有分类（不区分用户）
    // =========================
    @Operation(
            summary = "查询所有分类",
            description = "查询系统中所有分类（通常用于前台展示）"
    )
    @GetMapping("/all")
    public Result<List<Category>> getAllCategory() {

        List<Category> cs = categoryService.Alllist();
        return Result.success(cs);
    }
}
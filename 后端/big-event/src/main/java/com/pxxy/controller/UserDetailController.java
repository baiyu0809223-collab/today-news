package com.pxxy.controller;

import com.pxxy.pojo.Result;
import com.pxxy.pojo.UserDetail;
import com.pxxy.service.UserDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/detail")
@Tag(name = "用户个人名片接口")
@RequiredArgsConstructor
public class UserDetailController {

    private final UserDetailService userDetailService;

    @PostMapping
    @Operation(summary = "新增用户个人名片")
    public Result<Void> create(@RequestBody UserDetail detail) {
        userDetailService.create(detail);
        return Result.success();
    }

    @GetMapping
    @Operation(summary = "根据用户ID获取个人名片")
    public Result<UserDetail> getByUserId(@RequestParam Integer userId) {
        UserDetail detail = userDetailService.getByUserId(userId);
        return Result.success(detail);
    }

    @PutMapping
    @Operation(summary = "更新用户个人名片")
    public Result<Void> update(@RequestBody UserDetail detail) {
        userDetailService.update(detail);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "根据用户ID删除个人名片")
    public Result<Void> delete(@RequestParam Integer userId) {
        userDetailService.delete(userId);
        return Result.success();
    }
}
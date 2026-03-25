package com.pxxy.controller;

import com.pxxy.dto.RegisterDTO;
import com.pxxy.pojo.Result;
import com.pxxy.pojo.User;
import com.pxxy.pojo.UserDetail;
import com.pxxy.service.UserService;
import com.pxxy.utils.JwtUtil;
import com.pxxy.utils.Md5Util;
import com.pxxy.utils.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Tag(name = "用户管理", description = "用户注册、登录、资料管理接口")
@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // =========================
    // 注册
    // =========================
    @Operation(
            summary = "用户注册",
            description = "注册新用户（user + userDetail）",
            responses = {
                    @ApiResponse(responseCode = "200", description = "注册成功"),
                    @ApiResponse(responseCode = "400", description = "用户已存在")
            }
    )
    @PostMapping("/register")
    public Result register(
            @RequestBody @Valid RegisterDTO registerDTO
    ) {
        User user = registerDTO.getUser();
        UserDetail detail = registerDTO.getDetail();

        // 1. 用户是否存在
        User existUser = userService.findUserByUsername(user.getUsername());
        if (existUser != null) {
            return Result.error("注册失败，当前用户已存在！");
        }

        // 2. 注册（事务）
        userService.register(user, detail);

        return Result.success();
    }

    // =========================
    // 登录
    // =========================
    @Operation(
            summary = "用户登录",
            description = "登录成功后返回 JWT Token"
    )
    @PostMapping("/login")
    public Result login(
            @Parameter(description = "用户名", example = "testuser")
            @Pattern(regexp = "^\\S{5,16}$") String username,

            @Parameter(description = "密码", example = "123456")
            @Pattern(regexp = "^\\S{5,16}$") String password) {

        User user = userService.findUserByUsername(username);
        if (user == null) {
            return Result.error("用户名错误");
        }

        if (Md5Util.checkPassword(password, user.getPassword())) {

            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getId());
            claims.put("username", user.getUsername());
            claims.put("user_pic", user.getUserPic());
            claims.put("role", user.getRole());

            String token = JwtUtil.genToken(claims);

            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 12, TimeUnit.HOURS);

            return Result.success(token);
        } else {
            return Result.error("密码错误");
        }
    }

    // =========================
    // 获取当前用户信息
    // =========================
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/userInfo")
    public Result<User> userInfo() {

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User user = userService.findUserByUsername(username);
        return Result.success(user);
    }

    // =========================
    // 更新用户信息
    // =========================
    @Operation(summary = "更新用户信息")
    @PutMapping("/update")
    public Result update(
            @Parameter(description = "用户对象")
            @RequestBody @Validated User user) {

        userService.update(user);
        return Result.success();
    }

    // =========================
    // 更新头像
    // =========================
    @Operation(summary = "更新用户头像")
    @PatchMapping("/updateAvatar")
    public Result updateAvatar(
            @RequestParam  String avatarUrl) {

        userService.updateAvatar(avatarUrl);
        return Result.success(avatarUrl);
    }

    // =========================
    // 修改密码
    // =========================
    @Operation(summary = "修改用户密码")
    @PatchMapping("/updatePwd")
    public Result updatePwd(
            @Parameter(description = "密码参数 old_pwd / new_pwd / re_pwd")
            @RequestBody Map<String, String> params,

            @Parameter(description = "JWT Token")
            @RequestHeader(value = "Authorization") String token) {

        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd)
                || !StringUtils.hasLength(newPwd)
                || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺失必要的参数");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findUserByUsername(username);

        if (!user.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码错误");
        }

        if (!rePwd.equals(newPwd)) {
            return Result.error("两次输入的密码不一样！");
        }

        userService.updatePwd(newPwd);
        stringRedisTemplate.delete(token);

        return Result.success();
    }

    // =========================
    // 根据 ID 查询用户
    // =========================
    @Operation(summary = "根据ID查询用户")
    @GetMapping("/getUserById")
    public Result getUserById(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam("id") Integer id) {

        User user = userService.findUserById(id);
        return Result.success(user);
    }

    // =========================
    // 查询所有用户
    // =========================
    @Operation(summary = "获取所有用户")
    @GetMapping("/getAllUser")
    public Result getAllUser() {

        List<User> list = userService.findAllUser();
        return Result.success(list);
    }

    // =========================
    // 修改违规用户昵称
    // =========================
    @Operation(summary = "修改违规用户昵称")
    @PutMapping("/updateName")
    public Result updateName(
            @Parameter(description = "用户ID", example = "1")
            @RequestParam("id") Integer id) {

        userService.updateName(id);
        return Result.success();
    }
}
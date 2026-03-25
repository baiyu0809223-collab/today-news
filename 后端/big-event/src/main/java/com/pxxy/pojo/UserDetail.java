package com.pxxy.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "用户个人名片详情")
public class UserDetail {
    @Schema(description = "主键ID")
    private Integer id;
    @Schema(description = "用户ID（关联 user.id）", example = "14")
    private Integer userId;
    @Schema(description = "名片头像URL")
    private String avatar;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "展示用户名")
    private String username;
    @Schema(description = "个人简介")
    private String bio;
    @Schema(description = "性别：0-未知 1-男 2-女")
    private Integer gender;
    @Schema(description = "生日")
    private LocalDate birthday;
    @Schema(description = "所在地")
    private String location;
    @Schema(description = "个人主页")
    private String website;
    @Schema(description = "标签（逗号分隔）")
    private String tags;
    @Schema(description = "关注数")
    private Integer followCount;
    @Schema(description = "粉丝数")
    private Integer fanCount;
    @Schema(description = "获赞数")
    private Integer likeCount;
    @Schema(description = "最近活跃时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastActiveTime;
    @Schema(description = "是否认证：0-否 1-是")
    private Integer isVerified;
    @Schema(description = "用户状态：1-正常 0-禁用")
    private Integer status;
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
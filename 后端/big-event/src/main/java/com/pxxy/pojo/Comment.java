package com.pxxy.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Comment {
    private Integer id;
    @NotEmpty
    private Integer articleId;
    @NotEmpty
    private Integer userId;
    @NotEmpty
    private String username;
    private String userPic;
    @NotEmpty
    private String content;
    private Integer parentId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    @NotEmpty
    private String state; // enum: PENDING, APPROVED, REJECTED, DELETED
    @NotEmpty
    private Integer likeCount;
    @NotEmpty
    private Boolean isTop;
    @NotEmpty
    private List<Comment> replies;
}
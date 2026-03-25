package com.pxxy.pojo;


import com.pxxy.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
public class Examine {
    private Integer id;
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;
    @NotEmpty
    private String content;
    @URL
    @NotEmpty
    private String coverImg;
    @State
    private String state;
    @NotNull
    private Integer categoryId;
    private Integer createUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUsername;
    private String target;
    private Integer aid;
}

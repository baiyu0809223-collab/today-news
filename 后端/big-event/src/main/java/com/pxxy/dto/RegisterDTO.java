package com.pxxy.dto;

import com.pxxy.pojo.User;
import com.pxxy.pojo.UserDetail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotNull
    @Valid
    private User user;

    @NotNull
    @Valid
    private UserDetail detail;
}
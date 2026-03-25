package com.pxxy.service;

import com.pxxy.pojo.UserDetail;

public interface UserDetailService {

    void create(UserDetail detail);

    UserDetail getByUserId(Integer userId);

    void update(UserDetail detail);

    void delete(Integer userId);
}
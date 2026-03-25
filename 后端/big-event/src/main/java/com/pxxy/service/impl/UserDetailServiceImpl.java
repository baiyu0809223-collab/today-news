package com.pxxy.service.impl;

import com.pxxy.mapper.UserDetailMapper;
import com.pxxy.pojo.UserDetail;
import com.pxxy.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailServiceImpl implements UserDetailService {

    private final UserDetailMapper userDetailMapper;

    @Override
    public void create(UserDetail detail) {
        userDetailMapper.insert(detail);
    }

    @Override
    public UserDetail getByUserId(Integer userId) {
        return userDetailMapper.selectByUserId(userId);
    }

    @Override
    public void update(UserDetail detail) {
        userDetailMapper.update(detail);
    }

    @Override
    public void delete(Integer userId) {
        userDetailMapper.deleteByUserId(userId);
    }
}
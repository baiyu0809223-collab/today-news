package com.pxxy.service.impl;

import com.pxxy.mapper.UserMapper;
import com.pxxy.pojo.User;
import com.pxxy.pojo.UserDetail;
import com.pxxy.service.UserService;
import com.pxxy.utils.Md5Util;
import com.pxxy.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserByUsername(String username) {
        User u = userMapper.findUserByUserName(username);
        return u;
    }

//    @Override
//    public void registerUser(String username, String password) {
//        User user = new User();
//        //加密
//        String md5String= Md5Util.getMD5String(password);
//        userMapper.insertUser(username,md5String);
//    }

    @Override
    public void register(User user, UserDetail detail) {
        user.setPassword(Md5Util.getMD5String(user.getPassword()));
        user.setRole(0);
        userMapper.registerUser(user);
        detail.setUserId(userMapper.findUserByUserName(user.getUsername()).getId());
        detail.setAvatar(user.getUserPic());
        detail.setNickname(user.getNickname());
        detail.setUsername(user.getUsername());
        detail.setFollowCount(0);
        detail.setFanCount(0);
        detail.setStatus(1);
        detail.setLikeCount(0);
        detail.setIsVerified(0);
        detail.setLastActiveTime(LocalDateTime.now());
        userMapper.insertUserDetail(detail);
    }

    @Override
    public void registerGod(User user) {

    }

    @Override
    public void update(User user) {

        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map= ThreadLocalUtil.get();
        userMapper.updateAvatar(avatarUrl, (Integer) map.get("id"));

    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map= ThreadLocalUtil.get();
        userMapper.updatePwd(Md5Util.getMD5String(newPwd), (Integer) map.get("id"));
    }

    @Override
    public User findUserById(int id) {
        User u = userMapper.findUserById(id);
        return u;
    }

    @Override
    public List<User> findAllUser() {
        List<User> list=userMapper.findAllUser();
        return list;
    }

    @Override
    public void updateName(Integer id) {
        //更改的名称
        String new_name="违规名称_"+ UUID.randomUUID().toString().substring(0, 5);
        User u = userMapper.findUserById(id);
        u.setNickname(new_name);
        u.setUpdateTime(LocalDateTime.now());
        userMapper.update(u);
    }
}

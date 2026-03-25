package com.pxxy.service;

import com.pxxy.pojo.User;
import com.pxxy.pojo.UserDetail;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserService {
    //根据用户名查询用户
    public User findUserByUsername(String username);
    //注册
    //public void registerUser(String username,String password);
    public void register(User user, UserDetail detail);
    public void registerGod(User user);

    //用户更新
    public void update(User user);

    //更新头像
    public void updateAvatar(String avatarUrl);

    //更新密码
    public void updatePwd(String newPwd);

    //根据id获取用户
    public  User findUserById(int id);

    List<User> findAllUser();

    void updateName(Integer id);
}

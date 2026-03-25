package com.pxxy.mapper;

import com.pxxy.pojo.User;
import com.pxxy.pojo.UserDetail;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Mapper
public interface UserMapper {

    //通过用户名查找用户
    @Select("select * from user where username=#{username}")

    public User findUserByUserName(String username);


    //注册
    @Insert("insert into `user`(username,password,nickname,email,user_pic,create_time,update_time,role) values(#{username},#{password},#{nickname},#{email},#{userPic},now(),now(),#{role})")
    public void registerUser(User user);
    @Insert("insert into user_detail(user_id,avatar,nickname,username,bio,gender,birthday,location,website,tags,follow_count,fan_count,like_count,last_active_time,is_verified,status,create_time,update_time) values(#{userId},#{avatar},#{nickname},#{username},#{bio},#{gender},#{birthday},#{location},#{website},#{tags},#{followCount},#{fanCount},#{likeCount},#{lastActiveTime},#{isVerified},#{status},now(),now())")
    public void insertUserDetail(UserDetail userDetail);

    @Update("update user set nickname=#{nickname" +
            "},email=#{email},update_time=#{updateTime} where id=#{id}")
    public void update(User user);

    @Update("update user set user_pic=#{avatarUrl},update_time=now() where id=#{id}")
    public void updateAvatar(String avatarUrl, Integer id);

    @Update("update user set password=#{newPwd},user.update_time=now() where id=#{id}")
    public void updatePwd(String newPwd,Integer id);

    //通过id查找用户
    @Select("select * from user where id=#{id}")

    public User findUserById(Integer id);

    @Select("select * from user")
    List<User> findAllUser();


}

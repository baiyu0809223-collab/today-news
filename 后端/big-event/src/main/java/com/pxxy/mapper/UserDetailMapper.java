package com.pxxy.mapper;

import com.pxxy.pojo.UserDetail;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDetailMapper {

    // ✅ 新增
    @Insert("""
        INSERT INTO user_detail (
            user_id, avatar, nickname, username, bio, gender, birthday,
            location, website, tags, follow_count, fan_count, like_count,
            last_active_time, is_verified, status
        ) VALUES (
            #{userId}, #{avatar}, #{nickname}, #{username}, #{bio}, #{gender}, #{birthday},
            #{location}, #{website}, #{tags}, #{followCount}, #{fanCount}, #{likeCount},
            #{lastActiveTime}, #{isVerified}, #{status}
        )
    """)
    void insert(UserDetail detail);

    // ✅ 根据 userId 查询
    @Select("SELECT * FROM user_detail WHERE user_id = #{userId}")
    UserDetail selectByUserId(Integer userId);

    // ✅ 更新
    @Update("""
        UPDATE user_detail SET
            avatar = #{avatar},
            nickname = #{nickname},
            username = #{username},
            bio = #{bio},
            gender = #{gender},
            birthday = #{birthday},
            location = #{location},
            website = #{website},
            tags = #{tags},
            follow_count = #{followCount},
            fan_count = #{fanCount},
            like_count = #{likeCount},
            last_active_time = #{lastActiveTime},
            is_verified = #{isVerified},
            status = #{status}
        WHERE user_id = #{userId}
    """)
    void update(UserDetail detail);

    // ✅ 删除
    @Delete("DELETE FROM user_detail WHERE user_id = #{userId}")
    void deleteByUserId(Integer userId);
}
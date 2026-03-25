package com.pxxy.mapper;

import com.pxxy.pojo.Article;
import com.pxxy.pojo.Examine;
import org.apache.ibatis.annotations.*;


import java.util.List;

@Mapper
public interface ExamineMapper {

    //添加文章
    @Insert("insert into examine(title, content, category_id, create_user, create_time, update_time, cover_img, state,target,aid) " +
            "values (#{title},#{content},#{categoryId},#{createUser},#{createTime},#{updateTime},#{coverImg},#{state},#{target},#{aid})")
    void add(Examine examine);

    //删除审核文章
    @Delete("delete from examine where id=#{id}")
    void delete(Integer id);

    //查询列表
    @Select("<script>" +
            "SELECT e.*, u.username AS createUsername " +
            "FROM examine e " +
            "LEFT JOIN user u ON e.create_user = u.id " +
            "<where>" +
            "   <if test='categoryId != null'>e.category_id = #{categoryId}</if>" +
            "   <if test='createUser != null'> AND e.create_user = #{createUser}</if>" +
            "   <if test='state != null'> AND e.state = #{state}</if>" +
            "</where>" +
            "</script>")
    List<Examine> list(@Param("categoryId") String categoryId,  @Param("createUser") String createUser,@Param("state") String state);
}

package com.project.platform.mapper;

import com.project.platform.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    //查询user表所有数据
    @Select("select * from user")
    List<User> list();

    //添加用户
    int insert(User entity);

    //根据用户名查询用户
    @Select("select * from user where username=#{username}")
    User selectByUsername(String username);

    //根据id更新数据
    void updateById(User entity);

    //根据id删除数据
    boolean deleteById(List<Integer> ids);

    //分页查询
    List<User> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    //根据id查询用户
    @Select("select * from user where id = #{id}")
    User selectById(Integer id);
}

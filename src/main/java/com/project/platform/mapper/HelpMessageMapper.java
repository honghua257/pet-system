package com.project.platform.mapper;

import com.project.platform.entity.HelpMessage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface HelpMessageMapper {
    //查询user表所有数据
    @Select("select * from help_message")
    List<HelpMessage> list();

    //添加用户
    int insert(HelpMessage entity);

    //根据id更新数据
    void updateById(HelpMessage entity);

    //根据id删除数据
    boolean deleteById(List<Integer> ids);

    //分页查询
    List<HelpMessage> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    //根据id查询用户
    @Select("select * from help_message where id = #{id}")
    HelpMessage selectById(Integer id);
}

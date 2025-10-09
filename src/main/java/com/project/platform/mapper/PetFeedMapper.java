package com.project.platform.mapper;

import com.project.platform.entity.PetFeed;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PetFeedMapper {
    //查询pet_type表所有数据
    @Select("select * from pet_feed")
    List<PetFeed> list();

    //添加用户
    int insert(PetFeed entity);

    //根据id更新数据
    void updateById(PetFeed entity);

    //根据id删除数据
    boolean deleteById(List<Integer> ids);

    //分页查询
    List<PetFeed> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    //根据id查询用户
    @Select("select * from pet_feed where id = #{id}")
    PetFeed selectById(Integer id);


}

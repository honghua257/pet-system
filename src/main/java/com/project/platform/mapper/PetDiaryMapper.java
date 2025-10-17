package com.project.platform.mapper;

import com.project.platform.entity.PetDiary;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PetDiaryMapper {
    //查询pet_diary_type表所有数据
    List<PetDiary> list();

    //添加用户
    int insert(PetDiary entity);

    //根据id更新数据
    void updateById(PetDiary entity);

    //根据id删除数据
    boolean deleteById(List<Integer> ids);

    //分页查询
    List<PetDiary> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    //根据id查询用户
    PetDiary selectById(Integer id);


}

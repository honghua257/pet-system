package com.project.platform.mapper;

import com.project.platform.entity.PetFosterCare;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PetFosterCareMapper {
    //查询pet_type表所有数据
    @Select("select * from pet_foster_care")
    List<PetFosterCare> list();

    //添加用户
    int insert(PetFosterCare entity);

    //根据id更新数据
    void updateById(PetFosterCare entity);

    //根据id删除数据
    boolean deleteById(List<Integer> ids);

    //分页查询
    List<PetFosterCare> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    //根据id查询用户
    @Select("select * from pet_foster_care where id = #{id}")
    PetFosterCare selectById(Integer id);


}

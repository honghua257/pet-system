package com.project.platform.mapper;

import com.project.platform.entity.PetType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PetTypeMapper {
    //查询pet_type表所有数据
    @Select("select * from pet_type")
    List<PetType> list();

    //添加用户
    int insert(PetType entity);

    //根据id更新数据
    void updateById(PetType entity);

    //根据id删除数据
    boolean deleteById(List<Integer> ids);

    //分页查询
    List<PetType> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    //根据id查询用户
    @Select("select * from pet_type where id = #{id}")
    PetType selectById(Integer id);

    @Select("select * from pet_type where name = #{name} limit 1")
    PetType selectByName(@Param("name") String name);

    @Select("SELECT pt.name as pet_type_name, pt.id as pet_type_id, COUNT(p.id) as pet_count " +
            "FROM pet_type pt " +
            "LEFT JOIN pet p ON pt.id = p.pet_type_id " +
            "GROUP BY pt.id, pt.name " +
            "ORDER BY pet_count DESC, pt.id ASC")
    List<Map<String, Object>> getPetTypeDistribution();

}

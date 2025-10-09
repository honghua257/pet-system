package com.project.platform.mapper;

import com.project.platform.entity.PetStoreManager;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface PetStoreManagerMapper {
    List<PetStoreManager> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);

    int queryCount(@Param("query") Map<String,Object> query);

    @Select("select * from pet_store_manager where id =#{id}")
    PetStoreManager selectById(Integer id);

    @Select("select * from pet_store_manager")
    List<PetStoreManager> list();

    int insert(PetStoreManager entity);

    int updateById(PetStoreManager entity);

    boolean removeByIds(List<Integer> ids);

    @Select("select * from pet_store_manager WHERE username=#{username}")
    PetStoreManager selectByUsername(String username);

    @Select("select * from pet_store_manager where store_name = #{storeName}")
    PetStoreManager selectByStoreName(String storeName);
}





















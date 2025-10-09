package com.project.platform.mapper;

import com.project.platform.entity.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProductMapper {
    @Select("select * from product")
    List<Product> list();

    int insert(Product entity);

    void updateById(Product entity);

    boolean deleteById(List<Integer> ids);

    List<Product> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    @Select("select * from product where id = #{id}")
    Product selectById(Integer id);
}



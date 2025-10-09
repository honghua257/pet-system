package com.project.platform.mapper;

import com.project.platform.entity.ProductOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProductOrderMapper {
    @Select("select * from product_order")
    List<ProductOrder> list();

    int insert(ProductOrder entity);

    void updateById(ProductOrder entity);

    boolean deleteById(List<Integer> ids);

    List<ProductOrder> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    @Select("select * from product_order where id = #{id}")
    ProductOrder selectById(Integer id);
}

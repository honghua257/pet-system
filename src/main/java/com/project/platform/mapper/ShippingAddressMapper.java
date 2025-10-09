package com.project.platform.mapper;

import com.project.platform.entity.ShippingAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ShippingAddressMapper {
    @Select("select * from shipping_address")
    List<ShippingAddress> list();

    int insert(ShippingAddress entity);

    void updateById(ShippingAddress entity);

    boolean deleteById(List<Integer> ids);

    List<ShippingAddress> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    @Select("select * from shipping_address where id = #{id}")
    ShippingAddress selectById(Integer id);
}



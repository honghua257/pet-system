package com.project.platform.mapper;

import com.project.platform.entity.ProductOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface ProductOrderMapper {
    @Select("select * from product_order")
    List<ProductOrder> list();

  @Select("select * from product_order where username = #{username}")
    List<ProductOrder> listByUsername(@Param("username") String username);

  @Select("select * from product_order where user_id = #{userId}")
    List<ProductOrder> listByUserId(@Param("userId") Integer userId);

    int insert(ProductOrder entity);

    void updateById(ProductOrder entity);

    boolean deleteById(List<Integer> ids);

    List<ProductOrder> queryPage(@Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("query") Map<String, Object> query);
    int queryCount(@Param("query") Map<String, Object> query);

    @Select("select * from product_order where id = #{id}")
    ProductOrder selectById(Integer id);

  @Select("select * from product_order where id = #{id} and username = #{username}")
    ProductOrder selectByIdAndUsername(@Param("id") Integer id, @Param("username") String username);

  @Select("select * from product_order where id = #{id} and user_id = #{userId}")
    ProductOrder selectByIdAndUserId(@Param("id") Integer id, @Param("userId") Integer userId);

    @Select("SELECT DATE(create_time) as sale_date, SUM(price * quantity) as total_amount, COUNT(*) as order_count " +
            "FROM product_order " +
            "WHERE status = '已完成' " +
            "AND DATE(create_time) >= #{startDate} " +
            "AND DATE(create_time) <= #{endDate} " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY sale_date ASC")
    List<Map<String, Object>> getSalesData(@Param("startDate") String startDate, @Param("endDate") String endDate);
}

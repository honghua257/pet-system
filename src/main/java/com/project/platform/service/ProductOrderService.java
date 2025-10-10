package com.project.platform.service;

import com.project.platform.entity.ProductOrder;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface ProductOrderService {
    PageVO<ProductOrder> page(Map<String, Object> query, Integer pageNum, Integer pageSize);

    ProductOrder selectById(Integer id);

  ProductOrder selectByIdAndUsername(Integer id, String username);

    List<ProductOrder> list();

  List<ProductOrder> listByUsername(String username);

  List<ProductOrder> listByUserId(Integer userId);

  ProductOrder selectByIdAndUserId(Integer id, Integer userId);

    void insert(ProductOrder entity);

    void updateById(ProductOrder entity);

    void deleteById(List<Integer> ids);
}

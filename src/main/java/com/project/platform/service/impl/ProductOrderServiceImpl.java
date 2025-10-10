package com.project.platform.service.impl;

import com.project.platform.entity.ProductOrder;
import com.project.platform.mapper.ProductOrderMapper;
import com.project.platform.service.ProductOrderService;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {
    @Resource
    private ProductOrderMapper productOrderMapper;

    @Override
    public PageVO<ProductOrder> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<ProductOrder> page = new PageVO<>();
        List<ProductOrder> list = productOrderMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(productOrderMapper.queryCount(query));
        return page;
    }

    @Override
    public ProductOrder selectById(Integer id) {
        return productOrderMapper.selectById(id);
    }

    @Override
    public ProductOrder selectByIdAndUsername(Integer id, String username) {
        return productOrderMapper.selectByIdAndUsername(id, username);
    }

    @Override
    public List<ProductOrder> listByUserId(Integer userId) {
        return productOrderMapper.listByUserId(userId);
    }

    @Override
    public ProductOrder selectByIdAndUserId(Integer id, Integer userId) {
        return productOrderMapper.selectByIdAndUserId(id, userId);
    }

    @Override
    public List<ProductOrder> list() {
        return productOrderMapper.list();
    }

    @Override
    public List<ProductOrder> listByUsername(String username) {
        return productOrderMapper.listByUsername(username);
    }

    @Override
    public void insert(ProductOrder entity) {
        check(entity);
        productOrderMapper.insert(entity);
    }

    @Override
    public void updateById(ProductOrder entity) {
        check(entity);
        productOrderMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        productOrderMapper.deleteById(ids);
    }

    public void check(ProductOrder entity) {
        if (entity == null) {
            throw new com.project.platform.exception.CustomException("参数不能为空");
        }
        if (entity.getProductName() == null || entity.getProductName().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("商品名称不能为空");
        }
        if (entity.getUsername() == null || entity.getUsername().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("用户名称不能为空");
        }
        if (entity.getPrice() == null || entity.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new com.project.platform.exception.CustomException("价格必须大于0");
        }
        if (entity.getStatus() == null || entity.getStatus().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("状态不能为空");
        }
        if (entity.getReceiverName() == null || entity.getReceiverName().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("收货人姓名不能为空");
        }
        if (entity.getReceiverPhone() == null || entity.getReceiverPhone().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("收货人电话不能为空");
        }
        if (entity.getReceiverAddress() == null || entity.getReceiverAddress().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("收货人地址不能为空");
        }
    }
}

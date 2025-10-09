package com.project.platform.service.impl;

import com.project.platform.entity.Product;
import com.project.platform.mapper.ProductMapper;
import com.project.platform.service.ProductService;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductMapper productMapper;

    @Override
    public PageVO<Product> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<Product> page = new PageVO<>();
        List<Product> list = productMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(productMapper.queryCount(query));
        return page;
    }

    @Override
    public Product selectById(Integer id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<Product> list() {
        return productMapper.list();
    }

    @Override
    public void insert(Product entity) {
        check(entity);
        productMapper.insert(entity);
    }

    @Override
    public void updateById(Product entity) {
        check(entity);
        productMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        productMapper.deleteById(ids);
    }

    public void check(Product entity) {
        if (entity == null) {
            throw new com.project.platform.exception.CustomException("参数不能为空");
        }
        if (entity.getStatus() != null && entity.getStatus().trim().equals("上架")) {
            if (entity.getStock() == null || entity.getStock() <= 0) {
                throw new com.project.platform.exception.CustomException("上架商品库存必须大于0");
            }
        }
    }
}



package com.project.platform.service.impl;

import com.project.platform.entity.ShippingAddress;
import com.project.platform.mapper.ShippingAddressMapper;
import com.project.platform.service.ShippingAddressService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {
    @Resource
    private ShippingAddressMapper shippingAddressMapper;

    @Override
    public PageVO<ShippingAddress> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<ShippingAddress> page = new PageVO<>();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("username", CurrentUserThreadLocal.getCurrentUser().getUsername());
        }
        List<ShippingAddress> list = shippingAddressMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(shippingAddressMapper.queryCount(query));
        return page;
    }

    @Override
    public ShippingAddress selectById(Integer id) {
        return shippingAddressMapper.selectById(id);
    }

    @Override
    public List<ShippingAddress> list() {
        return shippingAddressMapper.list();
    }

    @Override
    public void insert(ShippingAddress entity) {
        check(entity);
        shippingAddressMapper.insert(entity);
    }

    @Override
    public void updateById(ShippingAddress entity) {
        check(entity);
        shippingAddressMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        shippingAddressMapper.deleteById(ids);
    }

    public void check(ShippingAddress entity) {
        if (entity == null) {
            throw new com.project.platform.exception.CustomException("参数不能为空");
        }
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("姓名不能为空");
        }
        if (entity.getPhone() == null || entity.getPhone().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("电话不能为空");
        }
        if (entity.getAddress() == null || entity.getAddress().trim().isEmpty()) {
            throw new com.project.platform.exception.CustomException("地址不能为空");
        }
        // 绑定当前用户名
        if (CurrentUserThreadLocal.getCurrentUser() != null) {
            entity.setUsername(CurrentUserThreadLocal.getCurrentUser().getUsername());
        }
    }
}



package com.project.platform.service.impl;

import com.project.platform.entity.Pet;
import com.project.platform.mapper.PetMapper;
import com.project.platform.service.PetService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PetServiceImpl implements PetService {
    @Resource
    private PetMapper petMapper;

    @Override
    public PageVO<Pet> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<Pet> page = new PageVO<>();
        if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")) {
            query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        }
        //执行分页查询操作
        List<Pet> list = petMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        //获取总条数
        page.setTotal(petMapper.queryCount(query));
        return page;
    }

    @Override
    public Pet selectById(Integer id) {
        Pet pet = petMapper.selectById(id);
        return pet;
    }

    @Override
    public List<Pet> list() {
        return petMapper.list();
    }

    @Override
    public void insert(Pet entity) {
        check(entity);
        petMapper.insert(entity);
    }

    @Override
    public void updateById(Pet entity) {
        check(entity);
        petMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        petMapper.deleteById(ids);
    }

    public void check(Pet entity) {
        // 设置当前用户ID
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
    }

}

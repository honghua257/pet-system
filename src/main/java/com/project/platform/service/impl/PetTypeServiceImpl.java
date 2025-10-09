package com.project.platform.service.impl;

import com.project.platform.entity.PetType;
import com.project.platform.mapper.PetTypeMapper;
import com.project.platform.service.PetTypeService;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PetTypeServiceImpl implements PetTypeService {
    @Resource
    private PetTypeMapper petTypeMapper;

    @Override
    public PageVO<PetType> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetType> page = new PageVO<>();
        //执行分页查询操作
        List<PetType> list = petTypeMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        //获取总条数
        page.setTotal(petTypeMapper.queryCount(query));
        return page;
    }

    @Override
    public PetType selectById(Integer id) {
        PetType petType = petTypeMapper.selectById(id);
        return petType;
    }

    @Override
    public List<PetType> list() {
        return petTypeMapper.list();
    }

    @Override
    public void insert(PetType entity) {
        check(entity);
        petTypeMapper.insert(entity);
    }

    @Override
    public void updateById(PetType entity) {
        check(entity);
        petTypeMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        petTypeMapper.deleteById(ids);
    }

    public void check(PetType entity) {
        if (entity.getName() == null || entity.getName().trim().isEmpty()) {
            throw new RuntimeException("名称不能为空");
        }
        PetType exist = petTypeMapper.selectByName(entity.getName().trim());
        // 如果已存在并且不是自己（更新场景要排除自身 id）
        if (exist != null && (entity.getId() == null || !exist.getId().equals(entity.getId()))) {
            throw new RuntimeException("名称已存在");
        }
    }

}

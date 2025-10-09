package com.project.platform.service.impl;

import com.project.platform.entity.PetFeed;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetFeedMapper;
import com.project.platform.service.PetFeedService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PetFeedServiceImpl implements PetFeedService {

    @Resource
    private PetFeedMapper petFeedMapper;
    @Override
    public PageVO<PetFeed> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetFeed> page = new PageVO<>();
        if(CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
            query.put("userId",CurrentUserThreadLocal.getCurrentUser().getId());
        } else if(CurrentUserThreadLocal.getCurrentUser().getType().equals("PET_STORE_MANAGER")) {
            query.put("petStoreManagerId",CurrentUserThreadLocal.getCurrentUser().getId());
        }
        //执行分页查询操作
        List<PetFeed> list = petFeedMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        //获取总条数
        page.setTotal(petFeedMapper.queryCount(query));
        return page;
    }

    @Override
    public PetFeed selectById(Integer id) {
        PetFeed petFeed = petFeedMapper.selectById(id);
        return petFeed;
    }

    @Override
    public List<PetFeed> list() {
        return petFeedMapper.list();
    }

    @Override
    public void insert(PetFeed entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        entity.setStatus("已下单");
        check(entity);
        petFeedMapper.insert(entity);
    }

    @Override
    public void updateById(PetFeed entity) {
        check(entity);
        petFeedMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        petFeedMapper.deleteById(ids);
    }

    @Override
    public void inService(Integer id) {
        PetFeed record = new PetFeed();
        record.setId(id);
        record.setStatus("服务中");
        petFeedMapper.updateById(record);
    }

    @Override
    public void finish(Integer id) {
        PetFeed record = new PetFeed();
        record.setId(id);
        record.setStatus("已完成");
        petFeedMapper.updateById(record);
    }
    public void check(PetFeed entity) {
        // 设置当前用户ID
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
    }
}

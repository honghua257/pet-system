package com.project.platform.service.impl;

import com.project.platform.entity.PetFosterCare;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetFosterCareMapper;
import com.project.platform.mapper.PetFosterCareMapper;
import com.project.platform.service.PetFosterCareService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PetFosterCareServiceImpl implements PetFosterCareService {

    @Resource
    private PetFosterCareMapper petFosterCareMapper;
    @Override
    public PageVO<PetFosterCare> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetFosterCare> page = new PageVO<>();
        if(CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
            query.put("userId",CurrentUserThreadLocal.getCurrentUser().getId());
        } else if(CurrentUserThreadLocal.getCurrentUser().getType().equals("PET_STORE_MANAGER")) {
            query.put("petStoreManagerId",CurrentUserThreadLocal.getCurrentUser().getId());
        }
        //执行分页查询操作
        List<PetFosterCare> list = petFosterCareMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        //获取总条数
        page.setTotal(petFosterCareMapper.queryCount(query));
        return page;
    }

    @Override
    public PetFosterCare selectById(Integer id) {
        PetFosterCare petFosterCare = petFosterCareMapper.selectById(id);
        return petFosterCare;
    }

    @Override
    public List<PetFosterCare> list() {
        return petFosterCareMapper.list();
    }

    @Override
    public void insert(PetFosterCare entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        entity.setStatus("已下单");
        check(entity);
        petFosterCareMapper.insert(entity);
    }

    @Override
    public void updateById(PetFosterCare entity) {
        check(entity);
        petFosterCareMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        petFosterCareMapper.deleteById(ids);
    }

    @Override
    public void inService(Integer id) {
        PetFosterCare record = new PetFosterCare();
        record.setId(id);
        record.setStatus("服务中");
        petFosterCareMapper.updateById(record);
    }

    @Override
    public void finish(Integer id) {
        PetFosterCare record = new PetFosterCare();
        record.setId(id);
        record.setStatus("已完成");
        petFosterCareMapper.updateById(record);
    }
    public void check(PetFosterCare entity) {
        if (entity.getReservedStartTime().isAfter(entity.getReservedEndTime())){
            throw new CustomException("开始时间不能大于结束时间");
        }
    }
}

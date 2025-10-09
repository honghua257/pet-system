package com.project.platform.service;

import com.project.platform.entity.PetFosterCare;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface PetFosterCareService {
    PageVO<PetFosterCare> page(Map<String,Object> query, Integer pageNum, Integer pageSize);

    PetFosterCare selectById(Integer id);

    List<PetFosterCare> list();

    void insert(PetFosterCare entity);

    void updateById(PetFosterCare entity);

    void deleteById(List<Integer> ids);

    void inService(Integer id);

    void finish(Integer id);
}

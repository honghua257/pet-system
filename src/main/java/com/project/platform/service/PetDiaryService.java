package com.project.platform.service;

import com.project.platform.entity.PetDiary;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface PetDiaryService {
    PageVO<PetDiary> page(Map<String,Object> query, Integer pageNum, Integer pageSize);

    PetDiary selectById(Integer id);

    List<PetDiary> list();

    void insert(PetDiary entity);

    void updateById(PetDiary entity);

    void deleteById(List<Integer> ids);
}

package com.project.platform.service;

import com.project.platform.entity.HelpMessage;
import com.project.platform.vo.PageVO;

import java.util.List;
import java.util.Map;

public interface HelpMessageService {
    PageVO<HelpMessage> page(Map<String,Object> query,Integer pageNum,Integer pageSize);

    HelpMessage selectById(Integer id);

    List<HelpMessage> list();

    void insert(HelpMessage entity);

    void updateById(HelpMessage entity);

    void deleteById(List<Integer> ids);
}

package com.project.platform.service.impl;

import com.project.platform.entity.HelpMessage;
import com.project.platform.entity.PetDiary;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetDiaryMapper;
import com.project.platform.service.PetDiaryService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PetDiaryServiceImpl implements PetDiaryService {
    @Resource
    private PetDiaryMapper petDiaryMapper;

    @Override
    public PageVO<PetDiary> page(Map<String, Object> query, Integer pageNum, Integer pageSize) {
        PageVO<PetDiary> page = new PageVO<>();
        //判断查询自己还是所有人
        if(query.containsKey("onlyYou")&& Boolean.valueOf(query.get("onlyYou").toString())){
            Integer userId = -1;
            if (CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
                //普通用户看自己的
                userId = CurrentUserThreadLocal.getCurrentUser().getId();
            }
            query.put("userId",userId);
        }
        //执行分页查询操作
        List<PetDiary> list = petDiaryMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        //获取总条数
        page.setTotal(petDiaryMapper.queryCount(query));
        return page;
    }

    @Override
    public PetDiary selectById(Integer id) {
        PetDiary pet = petDiaryMapper.selectById(id);
        return pet;
    }

    @Override
    public List<PetDiary> list() {
        return petDiaryMapper.list();
    }

    @Override
    public void insert(PetDiary entity) {
        if (!CurrentUserThreadLocal.getCurrentUser().getType().equals("USER")){
            throw new CustomException("普通用户才允许添加");
        }
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        check(entity);
        petDiaryMapper.insert(entity);
    }

    @Override
    public void updateById(PetDiary entity) {
        check(entity);
        petDiaryMapper.updateById(entity);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        petDiaryMapper.deleteById(ids);
    }

    public void check(PetDiary entity) {
        // 设置当前用户ID
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
    }

}

package com.project.platform.controller;

import com.project.platform.entity.PetDiary;
import com.project.platform.service.PetDiaryService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/petDiary")
public class PetDiaryController {
    @Resource
    private PetDiaryService petDiaryService;
    //分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<PetDiary>> page(
            @RequestParam Map<String,Object> query,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize
    ){
        PageVO<PetDiary> page = petDiaryService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    //根据id查询
    @GetMapping("selectById/{id}")
    public ResponseVO<PetDiary> selectById(@PathVariable("id") Integer id){
        PetDiary entity = petDiaryService.selectById(id);
        return ResponseVO.ok(entity);
    }
    //查询所有数据
    @GetMapping("list")
    public ResponseVO<List<PetDiary>> list(){
        return ResponseVO.ok(petDiaryService.list());
    }
    //新增
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetDiary entity){
        petDiaryService.insert(entity);
        return ResponseVO.ok();
    }

    //更新
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetDiary entity){
        petDiaryService.updateById(entity);
        return ResponseVO.ok();
    }

    //删除
    @DeleteMapping("delete")
    public ResponseVO delete(@RequestBody List<Integer> ids){
        petDiaryService.deleteById(ids);
        return ResponseVO.ok();
    }
}

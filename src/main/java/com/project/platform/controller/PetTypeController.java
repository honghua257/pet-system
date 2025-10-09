package com.project.platform.controller;


import com.project.platform.entity.PetType;
import com.project.platform.service.PetTypeService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/petType")
public class PetTypeController {
    @Resource
    private PetTypeService petTypeService;
    //分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<PetType>> page(
            @RequestParam Map<String,Object> query,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize
    ){
        PageVO<PetType> page = petTypeService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    //根据id查询
    @GetMapping("selectById/{id}")
    public ResponseVO<PetType> selectById(@PathVariable("id") Integer id){
        PetType entity = petTypeService.selectById(id);
        return ResponseVO.ok(entity);
    }
    //查询所有数据
    @GetMapping("list")
    public ResponseVO<List<PetType>> list(){
        return ResponseVO.ok(petTypeService.list());
    }
    //新增
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetType entity){
        petTypeService.insert(entity);
        return ResponseVO.ok();
    }

    //更新
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetType entity){
        petTypeService.updateById(entity);
        return ResponseVO.ok();
    }

    //删除
    @DeleteMapping("delete")
    public ResponseVO delete(@RequestBody List<Integer> ids){
        petTypeService.deleteById(ids);
        return ResponseVO.ok();
    }
}

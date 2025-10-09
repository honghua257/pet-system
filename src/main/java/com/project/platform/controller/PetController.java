package com.project.platform.controller;


import com.project.platform.entity.Pet;
import com.project.platform.service.PetService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pet")
public class PetController {
    @Resource
    private PetService petService;
    //分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<Pet>> page(
            @RequestParam Map<String,Object> query,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize
    ){
        PageVO<Pet> page = petService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    //根据id查询
    @GetMapping("selectById/{id}")
    public ResponseVO<Pet> selectById(@PathVariable("id") Integer id){
        Pet entity = petService.selectById(id);
        return ResponseVO.ok(entity);
    }
    //查询所有数据
    @GetMapping("list")
    public ResponseVO<List<Pet>> list(){
        return ResponseVO.ok(petService.list());
    }
    //新增
    @PostMapping("add")
    public ResponseVO add(@RequestBody Pet entity){
        petService.insert(entity);
        return ResponseVO.ok();
    }

    //更新
    @PutMapping("update")
    public ResponseVO update(@RequestBody Pet entity){
        petService.updateById(entity);
        return ResponseVO.ok();
    }

    //删除
    @DeleteMapping("delete")
    public ResponseVO delete(@RequestBody List<Integer> ids){
        petService.deleteById(ids);
        return ResponseVO.ok();
    }
}

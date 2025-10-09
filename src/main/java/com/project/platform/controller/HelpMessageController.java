package com.project.platform.controller;

import com.project.platform.entity.HelpMessage;
import com.project.platform.service.HelpMessageService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/helpMessage")
public class HelpMessageController {
    @Resource
    private HelpMessageService helpMessageService;
    //分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<HelpMessage>> page(
            @RequestParam Map<String,Object> query,
            @RequestParam(defaultValue = "1")Integer pageNum,
            @RequestParam(defaultValue = "10")Integer pageSize
    ){
        PageVO<HelpMessage> page = helpMessageService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    //根据id查询
    @GetMapping("selectById/{id}")
    public ResponseVO<HelpMessage> selectById(@PathVariable("id") Integer id){
        HelpMessage entity = helpMessageService.selectById(id);
        return ResponseVO.ok(entity);
    }
    //查询所有数据
    @GetMapping("list")
    public ResponseVO<List<HelpMessage>> list(){
        return ResponseVO.ok(helpMessageService.list());
    }
    //新增
    @PostMapping("add")
    public ResponseVO add(@RequestBody HelpMessage entity){
        helpMessageService.insert(entity);
        return ResponseVO.ok();
    }

    //更新
    @PutMapping("update")
    public ResponseVO update(@RequestBody HelpMessage entity){
        helpMessageService.updateById(entity);
        return ResponseVO.ok();
    }

    //删除
    @DeleteMapping("delete")
    public ResponseVO delete(@RequestBody List<Integer> ids){
        helpMessageService.deleteById(ids);
        return ResponseVO.ok();
    }
}

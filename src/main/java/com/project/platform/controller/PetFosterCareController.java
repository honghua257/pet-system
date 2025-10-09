package com.project.platform.controller;

import com.project.platform.entity.PetFosterCare;
import com.project.platform.service.PetFosterCareService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/petFosterCare")
public class PetFosterCareController {
    @Resource
    private PetFosterCareService petFeedService;

    // 分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<PetFosterCare>> page(
            @RequestParam Map<String, Object> query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        PageVO<PetFosterCare> page = petFeedService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    // 根据id查询
    @GetMapping("selectById/{id}")
    public ResponseVO<PetFosterCare> selectById(@PathVariable("id") Integer id) {
        PetFosterCare entity = petFeedService.selectById(id);
        return ResponseVO.ok(entity);
    }

    // 查询所有数据
    @GetMapping("list")
    public ResponseVO<List<PetFosterCare>> list() {
        return ResponseVO.ok(petFeedService.list());
    }

    // 新增
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetFosterCare entity) {
        petFeedService.insert(entity);
        return ResponseVO.ok();
    }

    // 更新
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetFosterCare entity) {
        petFeedService.updateById(entity);
        return ResponseVO.ok();
    }

    // 删除
    @DeleteMapping("delete")
    public ResponseVO delete(@RequestBody List<Integer> ids) {
        petFeedService.deleteById(ids);
        return ResponseVO.ok();
    }

    // 接单/服务中
    @PutMapping("inService/{id}")
    public ResponseVO inService(@PathVariable("id") Integer id) {
        petFeedService.inService(id);
        return ResponseVO.ok();
    }

    // 完成服务
    @PutMapping("finish/{id}")
    public ResponseVO finish(@PathVariable("id") Integer id) {
        petFeedService.finish(id);
        return ResponseVO.ok();
    }
}



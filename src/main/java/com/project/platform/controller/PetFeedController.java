package com.project.platform.controller;

import com.project.platform.entity.PetFeed;
import com.project.platform.service.PetFeedService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/petFeed")
public class PetFeedController {
    @Resource
    private PetFeedService petFeedService;

    // 分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<PetFeed>> page(
            @RequestParam Map<String, Object> query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ){
        PageVO<PetFeed> page = petFeedService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    // 根据id查询
    @GetMapping("selectById/{id}")
    public ResponseVO<PetFeed> selectById(@PathVariable("id") Integer id) {
        PetFeed entity = petFeedService.selectById(id);
        return ResponseVO.ok(entity);
    }

    // 查询所有数据
    @GetMapping("list")
    public ResponseVO<List<PetFeed>> list() {
        return ResponseVO.ok(petFeedService.list());
    }

    // 新增
    @PostMapping("add")
    public ResponseVO add(@RequestBody PetFeed entity) {
        petFeedService.insert(entity);
        return ResponseVO.ok();
    }

    // 更新
    @PutMapping("update")
    public ResponseVO update(@RequestBody PetFeed entity) {
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



package com.project.platform.controller;

import com.project.platform.entity.PetStoreManager;
import com.project.platform.service.PetStoreManagerService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/petStoreManager")
public class PetStoreManagerController {

    @Resource
    private PetStoreManagerService petStoreManagerService;

    // 查询所有宠物店管理员
    @GetMapping("list")
    public ResponseVO<List<PetStoreManager>> list() {
        List<PetStoreManager> list = petStoreManagerService.list();
        return ResponseVO.ok(list);
    }

    // 添加宠物店管理员
    @PostMapping("add")
    public ResponseVO<Void> add(@RequestBody PetStoreManager entity) {
        petStoreManagerService.insert(entity);
        return ResponseVO.ok();
    }

    // 更新宠物店管理员
    @PutMapping("update")
    public ResponseVO<Void> update(@RequestBody PetStoreManager entity) {
        petStoreManagerService.updateById(entity);
        return ResponseVO.ok();
    }

    // 删除宠物店管理员（支持批量）
    @DeleteMapping("delete")
    public ResponseVO<Void> delete(@RequestBody List<Integer> ids) {
        petStoreManagerService.removeByIds(ids);
        return ResponseVO.ok();
    }

    // 分页查询
    @GetMapping("page")
    public ResponseVO<PageVO<PetStoreManager>> page(
            @RequestParam Map<String, Object> query,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<PetStoreManager> page = petStoreManagerService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    // 根据 id 查询
    @GetMapping("selectById/{id}")
    public ResponseVO<PetStoreManager> selectById(@PathVariable("id") Integer id) {
        PetStoreManager entity = petStoreManagerService.selectById(id);
        return ResponseVO.ok(entity);
    }

}

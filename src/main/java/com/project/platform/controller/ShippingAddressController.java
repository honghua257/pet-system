package com.project.platform.controller;

import com.project.platform.entity.ShippingAddress;
import com.project.platform.service.ShippingAddressService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shippingAddress")
public class ShippingAddressController {

    @Resource
    private ShippingAddressService shippingAddressService;

    @GetMapping("page")
    public ResponseVO<PageVO<ShippingAddress>> page(@RequestParam Map<String, Object> query,
                                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseVO.ok(shippingAddressService.page(query, pageNum, pageSize));
    }

    @GetMapping("selectById/{id}")
    public ResponseVO<ShippingAddress> selectById(@PathVariable("id") Integer id) {
        return ResponseVO.ok(shippingAddressService.selectById(id));
    }

    @GetMapping("list")
    public ResponseVO<List<ShippingAddress>> list() {
        return ResponseVO.ok(shippingAddressService.list());
    }

    @PostMapping("add")
    public ResponseVO<Void> add(@RequestBody ShippingAddress entity) {
        shippingAddressService.insert(entity);
        return ResponseVO.ok();
    }

    @PutMapping("update")
    public ResponseVO<Void> update(@RequestBody ShippingAddress entity) {
        shippingAddressService.updateById(entity);
        return ResponseVO.ok();
    }

    @DeleteMapping("delete")
    public ResponseVO<Void> delete(@RequestBody List<Integer> ids) {
        shippingAddressService.deleteById(ids);
        return ResponseVO.ok();
    }
}



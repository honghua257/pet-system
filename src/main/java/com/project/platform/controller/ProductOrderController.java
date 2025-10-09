package com.project.platform.controller;

import com.project.platform.entity.ProductOrder;
import com.project.platform.service.ProductOrderService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/productOrder")
public class ProductOrderController {

    @Resource
    private ProductOrderService productOrderService;

    @GetMapping("page")
    public ResponseVO<PageVO<ProductOrder>> page(@RequestParam Map<String, Object> query,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<ProductOrder> page = productOrderService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    @GetMapping("selectById/{id}")
    public ResponseVO<ProductOrder> selectById(@PathVariable("id") Integer id) {
        return ResponseVO.ok(productOrderService.selectById(id));
    }

    @GetMapping("list")
    public ResponseVO<List<ProductOrder>> list() {
        return ResponseVO.ok(productOrderService.list());
    }

    @PostMapping("add")
    public ResponseVO<Void> add(@RequestBody ProductOrder entity) {
        productOrderService.insert(entity);
        return ResponseVO.ok();
    }

    @PutMapping("update")
    public ResponseVO<Void> update(@RequestBody ProductOrder entity) {
        productOrderService.updateById(entity);
        return ResponseVO.ok();
    }

    @DeleteMapping("delete")
    public ResponseVO<Void> delete(@RequestBody List<Integer> ids) {
        productOrderService.deleteById(ids);
        return ResponseVO.ok();
    }
}

package com.project.platform.controller;

import com.project.platform.entity.Product;
import com.project.platform.service.ProductService;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("page")
    public ResponseVO<PageVO<Product>> page(@RequestParam Map<String, Object> query,
                                            @RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<Product> page = productService.page(query, pageNum, pageSize);
        return ResponseVO.ok(page);
    }

    @GetMapping("selectById/{id}")
    public ResponseVO<Product> selectById(@PathVariable("id") Integer id) {
        return ResponseVO.ok(productService.selectById(id));
    }

    @GetMapping("list")
    public ResponseVO<List<Product>> list() {
        return ResponseVO.ok(productService.list());
    }

    @PostMapping("add")
    public ResponseVO<Void> add(@RequestBody Product entity) {
        productService.insert(entity);
        return ResponseVO.ok();
    }

    @PutMapping("update")
    public ResponseVO<Void> update(@RequestBody Product entity) {
        productService.updateById(entity);
        return ResponseVO.ok();
    }

    @DeleteMapping("delete")
    public ResponseVO<Void> delete(@RequestBody List<Integer> ids) {
        productService.deleteById(ids);
        return ResponseVO.ok();
    }
}



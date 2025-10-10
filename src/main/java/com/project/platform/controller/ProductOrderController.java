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
    public ResponseVO<ProductOrder> selectById(@PathVariable("id") Integer id,
                                               @RequestParam(required = false) String username,
                                               @RequestParam(required = false) Integer userId) {
        if (userId != null) {
            ProductOrder order = productOrderService.selectByIdAndUserId(id, userId);
            if (order == null) {
                return ResponseVO.fail(404, "订单不存在或无权限查看");
            }
            return ResponseVO.ok(order);
        }
        if (username != null && !username.trim().isEmpty()) {
            ProductOrder order = productOrderService.selectByIdAndUsername(id, username.trim());
            if (order == null) {
                return ResponseVO.fail(404, "订单不存在或无权限查看");
            }
            return ResponseVO.ok(order);
        }
        return ResponseVO.ok(productOrderService.selectById(id));
    }

    @GetMapping("list")
    public ResponseVO<List<ProductOrder>> list(@RequestParam(required = false) String username,
                                              @RequestParam(required = false) Integer userId) {
        if (userId != null) {
            return ResponseVO.ok(productOrderService.listByUserId(userId));
        }
        if (username != null && !username.trim().isEmpty()) {
            return ResponseVO.ok(productOrderService.listByUsername(username.trim()));
        }
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

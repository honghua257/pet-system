package com.project.platform.controller;

import com.project.platform.service.SalesService;
import com.project.platform.service.PetTypeDistributionService;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class SalesController {

    @Resource
    private SalesService salesService;

    @Resource
    private PetTypeDistributionService petTypeDistributionService;

    /**
     * 获取销售图表数据
     * @param days 查询天数 (3, 7, 15, 30)
     * @return 图表数据
     */
    @GetMapping("/sales/chart")
    public ResponseVO<Map<String, Object>> getSalesChart(@RequestParam(defaultValue = "7") Integer days) {
        Map<String, Object> chartData = salesService.getSalesChartData(days);
        return ResponseVO.ok(chartData);
    }

    /**
     * 获取宠物类型分布数据
     * @return 宠物类型分布数据
     */
    @GetMapping("/pet-type/distribution")
    public ResponseVO<List<Map<String, Object>>> getPetTypeDistribution() {
        List<Map<String, Object>> distributionData = petTypeDistributionService.getPetTypeDistribution();
        return ResponseVO.ok(distributionData);
    }
}
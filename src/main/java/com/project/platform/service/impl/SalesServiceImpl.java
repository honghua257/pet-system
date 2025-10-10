package com.project.platform.service.impl;

import com.project.platform.service.SalesService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 销售统计服务实现类
 */
@Service
public class SalesServiceImpl implements SalesService {

    @Resource
    private com.project.platform.mapper.ProductOrderMapper productOrderMapper;

    @Override
    public Map<String, Object> getSalesChartData(Integer days) {
        // 验证天数参数
        if (days == null || !Arrays.asList(3, 7, 15, 30).contains(days)) {
            days = 7; // 默认7天
        }

        // 计算日期范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        // 获取真实销售数据
        List<Map<String, Object>> salesData = productOrderMapper.getSalesData(
            startDate.toString(),
            endDate.plusDays(1).toString() // 加一天确保包含当天
        );

        // 准备图表数据
        List<String> dates = new ArrayList<>();
        List<BigDecimal> barData = new ArrayList<>();
        List<BigDecimal> trendData = new ArrayList<>();

        // 填充数据，确保每天都有数据（没有销售的日期为0）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, BigDecimal> salesMap = new HashMap<>();
        for (Map<String, Object> data : salesData) {
            java.sql.Date sqlDate = (java.sql.Date) data.get("sale_date");
            LocalDate localDate = sqlDate.toLocalDate();
            String date = localDate.format(formatter);
            String fullDateStr = localDate.format(fullFormatter);
            BigDecimal amount = (BigDecimal) data.get("total_amount");
            salesMap.put(fullDateStr, amount);
        }

        // 生成完整的时间序列数据
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dateStr = date.format(formatter);
            String fullDateStr = date.format(fullFormatter);

            dates.add(dateStr);
            BigDecimal amount = salesMap.getOrDefault(fullDateStr, BigDecimal.ZERO);
            barData.add(amount);
            trendData.add(amount);
        }

        // 返回图表数据
        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("barData", barData);
        result.put("trendData", trendData);
        result.put("title", "最近" + days + "天销售额趋势");

        return result;
    }
}
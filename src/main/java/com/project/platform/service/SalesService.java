package com.project.platform.service;

import java.util.Map;

/**
 * 销售统计服务接口
 */
public interface SalesService {

    /**
     * 获取销售图表数据
     * @param days 查询天数 (3, 7, 15, 30)
     * @return 图表数据
     */
    Map<String, Object> getSalesChartData(Integer days);
}
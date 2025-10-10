package com.project.platform.service;

import java.util.List;
import java.util.Map;

/**
 * 宠物类型分布统计服务接口
 */
public interface PetTypeDistributionService {

    /**
     * 获取宠物类型分布数据
     * @return 处理后的宠物类型分布数据
     */
    List<Map<String, Object>> getPetTypeDistribution();
}
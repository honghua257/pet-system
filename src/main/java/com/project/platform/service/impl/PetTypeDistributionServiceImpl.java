package com.project.platform.service.impl;

import com.project.platform.service.PetTypeDistributionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 宠物类型分布统计服务实现类
 */
@Service
public class PetTypeDistributionServiceImpl implements PetTypeDistributionService {

    @Resource
    private com.project.platform.mapper.PetTypeMapper petTypeMapper;

    @Override
    public List<Map<String, Object>> getPetTypeDistribution() {
        // 获取原始数据
        List<Map<String, Object>> rawData = petTypeMapper.getPetTypeDistribution();

        // 处理数据：如果类型数超过4个，将小类型合并为"其他"
        return processPetTypeData(rawData);
    }

    /**
     * 处理宠物类型数据，实现分组显示逻辑
     * @param rawData 原始数据
     * @return 处理后的数据
     */
    private List<Map<String, Object>> processPetTypeData(List<Map<String, Object>> rawData) {
        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> result = new ArrayList<>();

        // 如果类型数 <= 4，直接返回所有数据
        if (rawData.size() <= 4) {
            return rawData;
        }

        // 如果类型数 > 4，取前4个，其余合并为"其他"
        int otherCount = 0;
        List<String> otherTypeNames = new ArrayList<>();

        // 处理前4个类型
        for (int i = 0; i < Math.min(4, rawData.size()); i++) {
            Map<String, Object> typeData = rawData.get(i);
            result.add(typeData);
        }

        // 处理剩余类型，合并为"其他"
        for (int i = 4; i < rawData.size(); i++) {
            Map<String, Object> typeData = rawData.get(i);
            Number count = (Number) typeData.get("pet_count");
            otherCount += count.intValue();

            String typeName = (String) typeData.get("pet_type_name");
            if (typeName != null) {
                otherTypeNames.add(typeName);
            }
        }

        // 如果有其他类型，添加"其他"类别
        if (otherCount > 0) {
            Map<String, Object> otherData = new HashMap<>();
            otherData.put("pet_type_name", "其他");
            otherData.put("pet_type_id", -1); // 使用-1标识"其他"类别
            otherData.put("pet_count", otherCount);
            otherData.put("other_types", otherTypeNames); // 保存包含的类型名称，用于悬停显示
            result.add(otherData);
        }

        return result;
    }
}
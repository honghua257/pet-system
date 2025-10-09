package com.project.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PetFosterCare {
    private Integer id;
    private Integer petId;
    private Integer petTypeId;
    private Integer userId;
    private Integer petStoreManagerId;
    private LocalDateTime reservedStartTime;
    private LocalDateTime reservedEndTime;
    private String remark;
    private String status;
    private LocalDateTime createTime;
    // joined fields
    private String petTypeName;
    private String petStoreName;
    private String petName;
    private String username;
}

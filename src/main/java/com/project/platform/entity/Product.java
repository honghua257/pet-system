package com.project.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Product {
    private Integer id;
    private String name;
    private String mainImg;
    private String introduce;
    private String status;
    private Float price;
    private Integer stock;
    private Integer salesVolume;
    private LocalDateTime createTime;
}



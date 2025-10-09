package com.project.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShippingAddress {
    private Integer id;
    private String name;
    private String phone;
    private String username;
    private String address;
    private LocalDateTime createTime;
}



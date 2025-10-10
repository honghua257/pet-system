package com.project.platform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductOrder {
    private Integer id;
    private String productName;
    private String username;
    private Integer userId;
    private BigDecimal price;
    private Integer quantity;
    private String status;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private LocalDateTime createTime;
}

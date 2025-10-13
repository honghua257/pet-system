package com.project.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HelpMessage {
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private LocalDateTime createTime;
    private Integer replyCount; // 回复数量
    private String username; // 用户名（从user表关联查询）
    private String userAvatarUrl; // 用户头像（从user表关联查询）

  }

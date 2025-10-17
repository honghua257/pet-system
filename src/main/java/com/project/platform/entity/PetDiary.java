package com.project.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class PetDiary {
    private Integer id;
    private String title;
    private String content;
    private Integer userId;
    private String username;
    private String userAvatarUrl;
    private LocalDateTime createTime;
    private Integer replyCount; // 评论数量
}

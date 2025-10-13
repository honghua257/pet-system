package com.project.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HelpReply {
    private Integer id;
    private Integer helpId;
    private Integer userId;
    private String content;
    private Integer parentId;
    private LocalDateTime createTime;

    // 关联字段
    private String username;
    private String userAvatarUrl;
    private List<HelpReply> replies; // 子回复列表
}
package com.project.platform.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PetDiaryReply {
    private Integer id;
    private Integer diaryId;
    private Integer userId;
    private String content;
    private Integer parentId;
    private LocalDateTime createTime;

    // 关联字段
    private String username;
    private String userAvatarUrl;
    private List<PetDiaryReply> replies; // 子回复列表
}
package com.project.platform.service;

import com.project.platform.entity.PetDiaryReply;

import java.util.List;

public interface PetDiaryReplyService {

    /**
     * 根据日记ID获取评论列表（树形结构）
     */
    List<PetDiaryReply> getRepliesByDiaryId(Integer diaryId);

    /**
     * 添加评论
     */
    void addReply(PetDiaryReply reply);

    /**
     * 删除评论
     */
    void deleteReply(Integer id, Integer userId);

    /**
     * 更新评论
     */
    void updateReply(PetDiaryReply reply, Integer userId);
}
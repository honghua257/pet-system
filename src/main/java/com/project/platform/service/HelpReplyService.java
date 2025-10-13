package com.project.platform.service;

import com.project.platform.entity.HelpReply;

import java.util.List;

public interface HelpReplyService {

    /**
     * 根据求助ID获取回复列表（树形结构）
     */
    List<HelpReply> getRepliesByHelpId(Integer helpId);

    /**
     * 添加回复
     */
    void addReply(HelpReply reply);

    /**
     * 删除回复
     */
    void deleteReply(Integer id, Integer userId);

    /**
     * 更新回复
     */
    void updateReply(HelpReply reply, Integer userId);

}
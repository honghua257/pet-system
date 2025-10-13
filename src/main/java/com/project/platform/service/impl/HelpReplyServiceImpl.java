package com.project.platform.service.impl;

import com.project.platform.entity.HelpReply;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.HelpReplyMapper;
import com.project.platform.service.HelpReplyService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HelpReplyServiceImpl implements HelpReplyService {

    @Resource
    private HelpReplyMapper helpReplyMapper;

    @Override
    public List<HelpReply> getRepliesByHelpId(Integer helpId) {
        // 获取所有回复
        List<HelpReply> allReplies = helpReplyMapper.selectByHelpId(helpId);
        if (allReplies == null || allReplies.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建树形结构
        return buildReplyTree(allReplies);
    }

    @Override
    @Transactional
    public void addReply(HelpReply reply) {
        // 获取当前用户ID
        Integer currentUserId = CurrentUserThreadLocal.getCurrentUser().getId();
        if (currentUserId == null) {
            throw new CustomException("请先登录");
        }

        // 设置用户ID
        reply.setUserId(currentUserId);

        // 插入回复
        helpReplyMapper.insert(reply);
    }

    @Override
    @Transactional
    public void deleteReply(Integer id, Integer userId) {
        if (userId == null) {
            throw new CustomException("请先登录");
        }

        // 检查权限
        int permissionCount = helpReplyMapper.checkReplyPermission(id, userId);
        if (permissionCount == 0) {
            throw new CustomException("无权限删除该回复");
        }

        // 删除回复（包括子回复）
        helpReplyMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void updateReply(HelpReply reply, Integer userId) {
        if (userId == null) {
            throw new CustomException("请先登录");
        }

        // 检查权限
        int permissionCount = helpReplyMapper.checkReplyPermission(reply.getId(), userId);
        if (permissionCount == 0) {
            throw new CustomException("无权限修改该回复");
        }

        // 更新回复
        helpReplyMapper.updateById(reply);
    }

    /**
     * 构建回复树形结构
     */
    private List<HelpReply> buildReplyTree(List<HelpReply> replies) {
        Map<Integer, HelpReply> replyMap = new HashMap<>();
        List<HelpReply> rootReplies = new ArrayList<>();

        // 将所有回复放入Map
        for (HelpReply reply : replies) {
            replyMap.put(reply.getId(), reply);
            reply.setReplies(new ArrayList<>());
        }

        // 构建树形结构
        for (HelpReply reply : replies) {
            if (reply.getParentId() == null) {
                // 顶级回复
                rootReplies.add(reply);
            } else {
                // 子回复
                HelpReply parentReply = replyMap.get(reply.getParentId());
                if (parentReply != null) {
                    parentReply.getReplies().add(reply);
                } else {
                    // 如果找不到父回复，作为顶级回复
                    rootReplies.add(reply);
                }
            }
        }

        return rootReplies;
    }
}
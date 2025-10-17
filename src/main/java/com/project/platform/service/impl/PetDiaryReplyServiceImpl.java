package com.project.platform.service.impl;

import com.project.platform.entity.PetDiaryReply;
import com.project.platform.exception.CustomException;
import com.project.platform.mapper.PetDiaryReplyMapper;
import com.project.platform.service.PetDiaryReplyService;
import com.project.platform.utils.CurrentUserThreadLocal;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PetDiaryReplyServiceImpl implements PetDiaryReplyService {

    @Resource
    private PetDiaryReplyMapper petDiaryReplyMapper;

    @Override
    public List<PetDiaryReply> getRepliesByDiaryId(Integer diaryId) {
        // 获取所有评论
        List<PetDiaryReply> allReplies = petDiaryReplyMapper.selectByDiaryId(diaryId);
        if (allReplies == null || allReplies.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建树形结构
        return buildReplyTree(allReplies);
    }

    @Override
    @Transactional
    public void addReply(PetDiaryReply reply) {
        // 获取当前用户ID
        Integer currentUserId = CurrentUserThreadLocal.getCurrentUser().getId();
        if (currentUserId == null) {
            throw new CustomException("请先登录");
        }

        // 设置用户ID
        reply.setUserId(currentUserId);

        // 插入评论
        petDiaryReplyMapper.insert(reply);

        // 更新日记评论数量
        petDiaryReplyMapper.updateDiaryReplyCount(reply.getDiaryId(), 1);
    }

    @Override
    @Transactional
    public void deleteReply(Integer id, Integer userId) {
        if (userId == null) {
            throw new CustomException("请先登录");
        }

        // 检查权限
        int permissionCount = petDiaryReplyMapper.checkReplyPermission(id, userId);
        if (permissionCount == 0) {
            throw new CustomException("无权限删除该评论");
        }

        // 获取要删除的评论信息
        List<PetDiaryReply> allReplies = petDiaryReplyMapper.selectByDiaryId(null); // 需要添加获取单个评论的方法
        PetDiaryReply targetReply = findReplyById(allReplies, id);
        if (targetReply == null) {
            throw new CustomException("评论不存在");
        }

        // 计算要删除的评论数量（包括子评论）
        int deleteCount = countReplies(allReplies, id);

        // 删除评论（包括子评论）
        petDiaryReplyMapper.deleteById(id);

        // 更新日记评论数量
        petDiaryReplyMapper.updateDiaryReplyCount(targetReply.getDiaryId(), -deleteCount);
    }

    @Override
    @Transactional
    public void updateReply(PetDiaryReply reply, Integer userId) {
        if (userId == null) {
            throw new CustomException("请先登录");
        }

        // 检查权限
        int permissionCount = petDiaryReplyMapper.checkReplyPermission(reply.getId(), userId);
        if (permissionCount == 0) {
            throw new CustomException("无权限修改该评论");
        }

        // 更新评论
        petDiaryReplyMapper.updateById(reply);
    }

    /**
     * 构建评论树形结构
     */
    private List<PetDiaryReply> buildReplyTree(List<PetDiaryReply> replies) {
        Map<Integer, PetDiaryReply> replyMap = new HashMap<>();
        List<PetDiaryReply> rootReplies = new ArrayList<>();

        // 将所有评论放入Map
        for (PetDiaryReply reply : replies) {
            replyMap.put(reply.getId(), reply);
            reply.setReplies(new ArrayList<>());
        }

        // 构建树形结构
        for (PetDiaryReply reply : replies) {
            if (reply.getParentId() == null) {
                // 顶级评论
                rootReplies.add(reply);
            } else {
                // 子评论
                PetDiaryReply parentReply = replyMap.get(reply.getParentId());
                if (parentReply != null) {
                    parentReply.getReplies().add(reply);
                } else {
                    // 如果找不到父评论，作为顶级评论
                    rootReplies.add(reply);
                }
            }
        }

        return rootReplies;
    }

    /**
     * 根据ID查找评论
     */
    private PetDiaryReply findReplyById(List<PetDiaryReply> replies, Integer id) {
        for (PetDiaryReply reply : replies) {
            if (reply.getId().equals(id)) {
                return reply;
            }
            // 递归查找子评论
            PetDiaryReply found = findReplyById(reply.getReplies(), id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 计算评论数量（包括子评论）
     */
    private int countReplies(List<PetDiaryReply> replies, Integer parentId) {
        int count = 0;
        for (PetDiaryReply reply : replies) {
            if (reply.getId().equals(parentId)) {
                count++; // 当前评论
                count += countReplies(replies, reply.getId()); // 递归计算子评论
            }
        }
        return count;
    }
}
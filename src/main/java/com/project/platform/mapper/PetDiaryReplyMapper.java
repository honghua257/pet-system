package com.project.platform.mapper;

import com.project.platform.entity.PetDiaryReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PetDiaryReplyMapper {

    // 根据日记ID查询所有评论
    List<PetDiaryReply> selectByDiaryId(@Param("diaryId") Integer diaryId);

    // 插入评论
    int insert(PetDiaryReply reply);

    // 根据ID删除评论
    int deleteById(@Param("id") Integer id);

    // 批量删除评论（包括子评论）
    int deleteByDiaryId(@Param("diaryId") Integer diaryId);

    // 更新评论
    int updateById(PetDiaryReply reply);

    // 检查用户是否有权限删除该评论
    int checkReplyPermission(@Param("id") Integer id, @Param("userId") Integer userId);

    // 更新日记评论数量
    int updateDiaryReplyCount(@Param("diaryId") Integer diaryId, @Param("count") Integer count);
}
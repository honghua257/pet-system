package com.project.platform.mapper;

import com.project.platform.entity.HelpReply;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HelpReplyMapper {

    // 根据求助ID查询所有回复
    List<HelpReply> selectByHelpId(@Param("helpId") Integer helpId);

    // 插入回复
    int insert(HelpReply reply);

    // 根据ID删除回复
    int deleteById(@Param("id") Integer id);

    // 批量删除回复（包括子回复）
    int deleteByHelpId(@Param("helpId") Integer helpId);

    // 更新回复
    int updateById(HelpReply reply);

    // 检查用户是否有权限删除该回复
    int checkReplyPermission(@Param("id") Integer id, @Param("userId") Integer userId);
}
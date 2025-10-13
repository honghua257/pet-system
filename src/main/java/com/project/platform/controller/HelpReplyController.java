package com.project.platform.controller;

import com.project.platform.entity.HelpReply;
import com.project.platform.service.HelpReplyService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/helpReply")
public class HelpReplyController {

    @Resource
    private HelpReplyService helpReplyService;

    /**
     * 获取求助的回复列表
     */
    @GetMapping("/list/{helpId}")
    public ResponseVO<List<HelpReply>> getReplies(@PathVariable Integer helpId) {
        List<HelpReply> replies = helpReplyService.getRepliesByHelpId(helpId);
        return ResponseVO.ok(replies);
    }

    /**
     * 添加回复
     */
    @PostMapping("/add")
    public ResponseVO addReply(@RequestBody HelpReply reply) {
        try {
            helpReplyService.addReply(reply);
            return ResponseVO.ok("回复成功");
        } catch (Exception e) {
            return ResponseVO.fail(500, e.getMessage());
        }
    }

    /**
     * 删除回复
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO deleteReply(@PathVariable Integer id) {
        try {
            Integer currentUserId = CurrentUserThreadLocal.getCurrentUser().getId();
            helpReplyService.deleteReply(id, currentUserId);
            return ResponseVO.ok("删除成功");
        } catch (Exception e) {
            return ResponseVO.fail(500, e.getMessage());
        }
    }

    /**
     * 更新回复
     */
    @PutMapping("/update")
    public ResponseVO updateReply(@RequestBody HelpReply reply) {
        try {
            Integer currentUserId = CurrentUserThreadLocal.getCurrentUser().getId();
            helpReplyService.updateReply(reply, currentUserId);
            return ResponseVO.ok("更新成功");
        } catch (Exception e) {
            return ResponseVO.fail(500, e.getMessage());
        }
    }
}
package com.project.platform.controller;

import com.project.platform.entity.PetDiaryReply;
import com.project.platform.service.PetDiaryReplyService;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petDiaryReply")
public class PetDiaryReplyController {

    @Resource
    private PetDiaryReplyService petDiaryReplyService;

    /**
     * 获取日记的评论列表
     */
    @GetMapping("/list/{diaryId}")
    public ResponseVO<List<PetDiaryReply>> getReplies(@PathVariable Integer diaryId) {
        List<PetDiaryReply> replies = petDiaryReplyService.getRepliesByDiaryId(diaryId);
        return ResponseVO.ok(replies);
    }

    /**
     * 添加评论
     */
    @PostMapping("/add")
    public ResponseVO addReply(@RequestBody PetDiaryReply reply) {
        try {
            petDiaryReplyService.addReply(reply);
            return ResponseVO.ok("评论成功");
        } catch (Exception e) {
            return ResponseVO.fail(500, e.getMessage());
        }
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/delete/{id}")
    public ResponseVO deleteReply(@PathVariable Integer id) {
        try {
            Integer currentUserId = CurrentUserThreadLocal.getCurrentUser().getId();
            petDiaryReplyService.deleteReply(id, currentUserId);
            return ResponseVO.ok("删除成功");
        } catch (Exception e) {
            return ResponseVO.fail(500, e.getMessage());
        }
    }

    /**
     * 更新评论
     */
    @PutMapping("/update")
    public ResponseVO updateReply(@RequestBody PetDiaryReply reply) {
        try {
            Integer currentUserId = CurrentUserThreadLocal.getCurrentUser().getId();
            petDiaryReplyService.updateReply(reply, currentUserId);
            return ResponseVO.ok("更新成功");
        } catch (Exception e) {
            return ResponseVO.fail(500, e.getMessage());
        }
    }
}
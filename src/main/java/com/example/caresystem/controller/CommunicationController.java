package com.example.caresystem.controller;

import com.example.caresystem.entity.Communication;
import com.example.caresystem.service.CommunicationService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/communication")
public class CommunicationController {

    @Autowired
    private CommunicationService communicationService;

    @PostMapping("/send")
    public Result<Communication> sendMessage(@RequestBody Communication communication,
                                             @RequestParam Integer childId,
                                             @RequestParam Integer sendId,
                                             @RequestParam Integer receiveId) {
        Communication savedCommunication = communicationService.sendMessage(communication, childId, sendId, receiveId);
        return Result.success(savedCommunication);
    }

    @GetMapping("/{id}")
    public Result<Communication> getCommunicationById(@PathVariable Integer id) {
        Communication communication = communicationService.getCommunicationById(id);
        return Result.success(communication);
    }

    @GetMapping("/list")
    public Result<List<Communication>> getAllCommunications() {
        List<Communication> communications = communicationService.getAllCommunications();
        return Result.success(communications);
    }

    @GetMapping("/child/{childId}")
    public Result<List<Communication>> getCommunicationsByChild(@PathVariable Integer childId) {
        List<Communication> communications = communicationService.getCommunicationsByChild(childId);
        return Result.success(communications);
    }

    @GetMapping("/sender/{sendId}")
    public Result<List<Communication>> getCommunicationsBySender(@PathVariable Integer sendId) {
        List<Communication> communications = communicationService.getCommunicationsBySender(sendId);
        return Result.success(communications);
    }

    @GetMapping("/receiver/{receiveId}")
    public Result<List<Communication>> getCommunicationsByReceiver(@PathVariable Integer receiveId) {
        List<Communication> communications = communicationService.getCommunicationsByReceiver(receiveId);
        return Result.success(communications);
    }

    @GetMapping("/unreplied")
    public Result<List<Communication>> getUnrepliedCommunications() {
        List<Communication> communications = communicationService.getUnrepliedCommunications();
        return Result.success(communications);
    }

    @GetMapping("/chat")
    public Result<List<Communication>> getChatHistory(@RequestParam Integer userId1,
                                                      @RequestParam Integer userId2) {
        List<Communication> communications = communicationService.getChatHistory(userId1, userId2);
        return Result.success(communications);
    }

    @PutMapping("/reply/{commId}")
    public Result<Communication> replyMessage(@PathVariable Integer commId,
                                              @RequestParam String replyContent) {
        Communication communication = communicationService.replyMessage(commId, replyContent);
        return Result.success(communication);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteCommunication(@PathVariable Integer id) {
        communicationService.deleteCommunication(id);
        return Result.success();
    }

    @GetMapping("/teacher/{teacherId}/parent/{parentId}/child/{childId}")
    public Result<List<Communication>> getTeacherParentChat(@PathVariable Integer teacherId,
                                                          @PathVariable Integer parentId,
                                                          @PathVariable Integer childId) {
        List<Communication> communications = communicationService.getTeacherParentChat(teacherId, parentId, childId);
        return Result.success(communications);
    }
}
package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.Communication;
import com.example.caresystem.entity.User;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.CommunicationRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommunicationService {

    @Autowired
    private CommunicationRepository communicationRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Communication sendMessage(Communication communication, Integer childId, Integer sendId, Integer receiveId) {
        if (childId == null) {
            throw new RuntimeException("儿童ID不能为空");
        }
        if (sendId == null) {
            throw new RuntimeException("发送人ID不能为空");
        }
        if (receiveId == null) {
            throw new RuntimeException("接收人ID不能为空");
        }
        if (!StringUtils.hasText(communication.getMessageContent())) {
            throw new RuntimeException("消息内容不能为空");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        communication.setChild(child);

        User sender = userRepository.findById(sendId)
                .orElseThrow(() -> new RuntimeException("发送人不存在"));
        communication.setSender(sender);

        User receiver = userRepository.findById(receiveId)
                .orElseThrow(() -> new RuntimeException("接收人不存在"));
        communication.setReceiver(receiver);

        communication.setReplyStatus(0);
        communication.setSendTime(LocalDateTime.now());

        return communicationRepository.save(communication);
    }

    public Communication getCommunicationById(Integer id) {
        return communicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("沟通记录不存在"));
    }

    public List<Communication> getAllCommunications() {
        return communicationRepository.findAll();
    }

    public List<Communication> getCommunicationsByChild(Integer childId) {
        return communicationRepository.findByChildId(childId);
    }

    public List<Communication> getCommunicationsBySender(Integer sendId) {
        return communicationRepository.findBySenderId(sendId);
    }

    public List<Communication> getCommunicationsByReceiver(Integer receiveId) {
        return communicationRepository.findByReceiverId(receiveId);
    }

    public List<Communication> getUnrepliedCommunications() {
        return communicationRepository.findUnrepliedCommunications();
    }

    public List<Communication> getChatHistory(Integer userId1, Integer userId2) {
        return communicationRepository.findChatHistory(userId1, userId2);
    }

    @Transactional
    public Communication replyMessage(Integer commId, String replyContent) {
        Communication communication = getCommunicationById(commId);
        communication.setReplyContent(replyContent);
        communication.setReplyStatus(1);
        communication.setReplyTime(LocalDateTime.now());
        return communicationRepository.save(communication);
    }

    @Transactional
    public void deleteCommunication(Integer id) {
        Communication communication = getCommunicationById(id);
        communicationRepository.delete(communication);
    }

    public List<Communication> getTeacherParentChat(Integer teacherId, Integer parentId, Integer childId) {
        return communicationRepository.findTeacherParentChat(teacherId, parentId, childId);
    }
}
package com.example.caresystem.repository;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.Communication;
import com.example.caresystem.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 沟通记录数据访问接口
 * @author rhy
 */
@Repository
public interface CommunicationRepository extends JpaRepository<Communication, Integer>, JpaSpecificationExecutor<Communication> {

    /**
     * 根据儿童查找沟通记录
     * @param child 儿童
     * @return 沟通记录列表
     */
    List<Communication> findByChild(Child child);

    /**
     * 根据发送人查找沟通记录
     * @param sender 发送人
     * @return 沟通记录列表
     */
    List<Communication> findBySender(User sender);

    /**
     * 根据接收人查找沟通记录
     * @param receiver 接收人
     * @return 沟通记录列表
     */
    List<Communication> findByReceiver(User receiver);

    /**
     * 根据回复状态查找沟通记录
     * @param replyStatus 回复状态
     * @return 沟通记录列表
     */
    List<Communication> findByReplyStatus(Integer replyStatus);

    /**
     * 根据儿童ID查找沟通记录
     * @param childId 儿童ID
     * @return 沟通记录列表
     */
    @Query("SELECT c FROM Communication c WHERE c.child.childId = :childId")
    List<Communication> findByChildId(@Param("childId") Integer childId);

    /**
     * 根据发送人ID查找沟通记录
     * @param sendId 发送人ID
     * @return 沟通记录列表
     */
    @Query("SELECT c FROM Communication c WHERE c.sender.userId = :sendId")
    List<Communication> findBySenderId(@Param("sendId") Integer sendId);

    /**
     * 根据接收人ID查找沟通记录
     * @param receiveId 接收人ID
     * @return 沟通记录列表
     */
    @Query("SELECT c FROM Communication c WHERE c.receiver.userId = :receiveId")
    List<Communication> findByReceiverId(@Param("receiveId") Integer receiveId);

    /**
     * 查找未回复的沟通记录
     * @return 未回复记录列表
     */
    @Query("SELECT c FROM Communication c WHERE c.replyStatus = 0 ORDER BY c.sendTime DESC")
    List<Communication> findUnrepliedCommunications();

    /**
     * 查找双方的沟通记录
     * @param userId1 用户1ID
     * @param userId2 用户2ID
     * @return 沟通记录列表
     */
    @Query("SELECT c FROM Communication c WHERE " +
            "(c.sender.userId = :userId1 AND c.receiver.userId = :userId2) OR " +
            "(c.sender.userId = :userId2 AND c.receiver.userId = :userId1) " +
            "ORDER BY c.sendTime DESC")
    List<Communication> findChatHistory(@Param("userId1") Integer userId1,
                                        @Param("userId2") Integer userId2);

    /**
     * 查找教师与家长的沟通记录
     * @param teacherId 教师ID
     * @param parentId 家长ID
     * @param childId 儿童ID
     * @return 沟通记录列表
     */
    @Query("SELECT c FROM Communication c WHERE " +
            "((c.sender.userId = :teacherId AND c.receiver.userId = :parentId) OR " +
            "(c.sender.userId = :parentId AND c.receiver.userId = :teacherId)) AND " +
            "c.child.childId = :childId " +
            "ORDER BY c.sendTime DESC")
    List<Communication> findTeacherParentChat(@Param("teacherId") Integer teacherId,
                                              @Param("parentId") Integer parentId,
                                              @Param("childId") Integer childId);

    /**
     * 统计未回复消息数量
     * @param userId 用户ID
     * @return 未回复消息数量
     */
    @Query("SELECT COUNT(c) FROM Communication c WHERE c.receiver.userId = :userId AND c.replyStatus = 0")
    Long countUnreadMessages(@Param("userId") Integer userId);

    /**
     * 统计用户发送的消息数量
     * @param userId 用户ID
     * @return 发送消息数量
     */
    @Query("SELECT COUNT(c) FROM Communication c WHERE c.sender.userId = :userId")
    Long countSentMessages(@Param("userId") Integer userId);

    /**
     * 统计用户接收的消息数量
     * @param userId 用户ID
     * @return 接收消息数量
     */
    @Query("SELECT COUNT(c) FROM Communication c WHERE c.receiver.userId = :userId")
    Long countReceivedMessages(@Param("userId") Integer userId);

    /**
     * 更新回复信息
     * @param commId 沟通ID
     * @param replyContent 回复内容
     * @param replyTime 回复时间
     * @param replyStatus 回复状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Communication c SET c.replyContent = :replyContent, c.replyTime = :replyTime, c.replyStatus = :replyStatus, c.updateTime = :updateTime WHERE c.commId = :commId")
    int updateReply(@Param("commId") Integer commId,
                    @Param("replyContent") String replyContent,
                    @Param("replyTime") LocalDateTime replyTime,
                    @Param("replyStatus") Integer replyStatus,
                    @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量更新已读状态
     * @param commIds 沟通ID列表
     * @param replyStatus 回复状态
     * @param updateTime 更新时间
     * @return 更新记录数
     */
    @Modifying
    @Transactional
    @Query("UPDATE Communication c SET c.replyStatus = :replyStatus, c.updateTime = :updateTime WHERE c.commId IN :commIds")
    int batchUpdateReplyStatus(@Param("commIds") List<Integer> commIds,
                               @Param("replyStatus") Integer replyStatus,
                               @Param("updateTime") LocalDateTime updateTime);

    /**
     * 分页查询用户沟通记录
     * @param userId 用户ID
     * @param pageable 分页参数
     * @return 沟通记录分页数据
     */
    @Query("SELECT c FROM Communication c WHERE c.sender.userId = :userId OR c.receiver.userId = :userId ORDER BY c.sendTime DESC")
    Page<Communication> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    /**
     * 根据发送时间范围查询沟通记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 沟通记录列表
     */
    List<Communication> findBySendTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查找最近聊天对象
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近聊天用户ID列表
     */
    @Query(value = "SELECT DISTINCT " +
            "CASE WHEN c.sender_id = :userId THEN c.receive_id ELSE c.sender_id END as other_user " +
            "FROM t_communication c " +
            "WHERE c.sender_id = :userId OR c.receive_id = :userId " +
            "ORDER BY c.send_time DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Integer> findRecentChatUsers(@Param("userId") Integer userId,
                                      @Param("limit") Integer limit);
}

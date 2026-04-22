package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.DailyStatus;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.DailyStatusRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailyStatusService {

    @Autowired
    private DailyStatusRepository dailyStatusRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DailyStatus addDailyStatus(DailyStatus dailyStatus, Integer childId, Integer teacherId) {
        if (childId == null) {
            throw new RuntimeException("儿童ID不能为空");
        }
        if (teacherId == null) {
            throw new RuntimeException("教师ID不能为空");
        }
        if (dailyStatus.getRecordDate() == null) {
            throw new RuntimeException("记录日期不能为空");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        dailyStatus.setChild(child);

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("教师不存在"));
        if (!UserEnums.Role.TEACHER.getCode().equals(teacher.getRoleType())) {
            throw new RuntimeException("该用户不是教师角色");
        }
        dailyStatus.setTeacher(teacher);

        // 如果 recordType 不为空且 content 不为空，则映射到旧字段以保持兼容
        if (dailyStatus.getRecordType() != null && StringUtils.hasText(dailyStatus.getContent())) {
            switch (dailyStatus.getRecordType()) {
                case 1: // 日常表现
                case 4: // 活动表现
                    dailyStatus.setActivity(dailyStatus.getContent());
                    break;
                case 2: // 饮食情况
                    dailyStatus.setDiet(dailyStatus.getContent());
                    break;
                case 5: // 健康状况
                    dailyStatus.setAbnormalDesc(dailyStatus.getContent());
                    break;
            }
        }

        dailyStatus.setPushStatus(0);

        return dailyStatusRepository.save(dailyStatus);
    }

    public DailyStatus getDailyStatusById(Integer id) {
        return dailyStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("日常记录不存在"));
    }

    public List<DailyStatus> getAllDailyStatuses() {
        return dailyStatusRepository.findAll();
    }

    public List<DailyStatus> getDailyStatusesByChild(Integer childId) {
        return dailyStatusRepository.findByChildId(childId);
    }

    public List<DailyStatus> getDailyStatusesByTeacher(Integer teacherId) {
        return dailyStatusRepository.findByTeacher(userRepository.findById(teacherId).orElse(null));
    }

    public List<DailyStatus> getDailyStatusesByDate(LocalDate recordDate) {
        return dailyStatusRepository.findByRecordDate(recordDate);
    }

    public DailyStatus getDailyStatusByChildAndDate(Integer childId, LocalDate recordDate) {
        return dailyStatusRepository.findByChildIdAndRecordDate(childId, recordDate)
                .orElseThrow(() -> new RuntimeException("该日期的记录不存在"));
    }

    public List<DailyStatus> getTodayDailyStatuses() {
        return dailyStatusRepository.findTodayDailyStatus();
    }

    public List<DailyStatus> getAbnormalRecords() {
        return dailyStatusRepository.findAbnormalRecords();
    }

    @Transactional
    public DailyStatus updateDailyStatus(Integer id, DailyStatus dailyStatus) {
        DailyStatus oldStatus = getDailyStatusById(id);

        if (dailyStatus.getRecordType() != null) {
            oldStatus.setRecordType(dailyStatus.getRecordType());
        }
        if (dailyStatus.getContent() != null) {
            oldStatus.setContent(dailyStatus.getContent());
        }
        if (dailyStatus.getDiet() != null) {
            oldStatus.setDiet(dailyStatus.getDiet());
        }
        if (dailyStatus.getHomework() != null) {
            oldStatus.setHomework(dailyStatus.getHomework());
        }
        if (dailyStatus.getActivity() != null) {
            oldStatus.setActivity(dailyStatus.getActivity());
        }
        if (StringUtils.hasText(dailyStatus.getAbnormalType())) {
            oldStatus.setAbnormalType(dailyStatus.getAbnormalType());
        }
        if (dailyStatus.getAbnormalDesc() != null) {
            oldStatus.setAbnormalDesc(dailyStatus.getAbnormalDesc());
        }
        if (dailyStatus.getAbnormalImg() != null) {
            oldStatus.setAbnormalImg(dailyStatus.getAbnormalImg());
        }

        return dailyStatusRepository.save(oldStatus);
    }

    @Transactional
    public DailyStatus pushToParent(Integer id) {
        DailyStatus dailyStatus = getDailyStatusById(id);
        dailyStatus.setPushStatus(1);
        dailyStatus.setPushTime(LocalDateTime.now());
        return dailyStatusRepository.save(dailyStatus);
    }

    @Transactional
    public void deleteDailyStatus(Integer id) {
        DailyStatus dailyStatus = getDailyStatusById(id);
        dailyStatusRepository.delete(dailyStatus);
    }

    public List<DailyStatus> getDailyStatusesByDateRange(Integer childId, LocalDate startDate, LocalDate endDate) {
        return dailyStatusRepository.findByChildIdAndDateRange(childId, startDate, endDate);
    }
}
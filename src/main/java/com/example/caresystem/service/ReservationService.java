package com.example.caresystem.service;

import com.example.caresystem.entity.Child;
import com.example.caresystem.entity.Reservation;
import com.example.caresystem.entity.User;
import com.example.caresystem.enums.BusinessEnums;
import com.example.caresystem.enums.UserEnums;
import com.example.caresystem.repository.ChildRepository;
import com.example.caresystem.repository.ReservationRepository;
import com.example.caresystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Reservation addReservation(Reservation reservation, Integer childId, Integer parentId) {
        if (childId == null) {
            throw new RuntimeException("儿童ID不能为空");
        }
        if (parentId == null) {
            throw new RuntimeException("家长ID不能为空");
        }
        if (reservation.getReserveDate() == null) {
            throw new RuntimeException("预约日期不能为空");
        }
        if (!StringUtils.hasText(reservation.getTimeSlot())) {
            throw new RuntimeException("时间段不能为空");
        }

        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        reservation.setChild(child);

        User parent = userRepository.findById(parentId)
                .orElseThrow(() -> new RuntimeException("家长不存在"));
        if (!UserEnums.Role.PARENT.getCode().equals(parent.getRoleType())) {
            throw new RuntimeException("该用户不是家长角色");
        }
        reservation.setParent(parent);

        reservation.setReserveStatus(BusinessEnums.ReservationStatus.REVIEW.getCode());

        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Integer id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("预约记录不存在"));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public List<Reservation> getReservationsByChild(Integer childId) {
        return reservationRepository.findByChildId(childId);
    }

    public List<Reservation> getReservationsByParent(Integer parentId) {
        return reservationRepository.findByParentId(parentId);
    }

    public List<Reservation> getReservationsByDate(LocalDate reserveDate) {
        return reservationRepository.findByReserveDate(reserveDate);
    }

    public List<Reservation> getReservationsByStatus(String reserveStatus) {
        return reservationRepository.findByReserveStatus(reserveStatus);
    }

    public List<Reservation> getReservationsByChildAndStatus(Integer childId, String reserveStatus) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        return reservationRepository.findByChildAndReserveStatus(child, reserveStatus);
    }

    public List<Reservation> getReservationsByChildAndDate(Integer childId, LocalDate reserveDate) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new RuntimeException("儿童不存在"));
        return reservationRepository.findByChildAndReserveDate(child, reserveDate);
    }

    @Transactional
    public Reservation updateReservation(Integer id, Reservation reservation) {
        Reservation oldReservation = getReservationById(id);

        if (reservation.getReserveDate() != null) {
            oldReservation.setReserveDate(reservation.getReserveDate());
        }
        if (StringUtils.hasText(reservation.getTimeSlot())) {
            oldReservation.setTimeSlot(reservation.getTimeSlot());
        }
        if (reservation.getSpecialNeeds() != null) {
            oldReservation.setSpecialNeeds(reservation.getSpecialNeeds());
        }

        return reservationRepository.save(oldReservation);
    }

    @Transactional
    public Reservation auditReservation(Integer id, Integer auditorId, String status) {
        Reservation reservation = getReservationById(id);

        User auditor = userRepository.findById(auditorId)
                .orElseThrow(() -> new RuntimeException("审核人不存在"));
        if (!UserEnums.Role.TEACHER.getCode().equals(auditor.getRoleType()) &&
            !UserEnums.Role.ADMIN.getCode().equals(auditor.getRoleType())) {
            throw new RuntimeException("只有教师或管理员可以审核预约");
        }

        reservation.setAuditor(auditor);
        reservation.setAuditTime(LocalDateTime.now());
        reservation.setReserveStatus(status);

        return reservationRepository.save(reservation);
    }

    @Transactional
    public void deleteReservation(Integer id) {
        Reservation reservation = getReservationById(id);
        reservationRepository.delete(reservation);
    }

    @Transactional
    public Reservation cancelReservation(Integer id) {
        Reservation reservation = getReservationById(id);
        reservation.setReserveStatus(BusinessEnums.ReservationStatus.CANCEL.getCode());
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getTodayReservations() {
        return reservationRepository.findByReserveDate(LocalDate.now());
    }

    public List<Reservation> getPendingReservations() {
        return reservationRepository.findByReserveStatus(BusinessEnums.ReservationStatus.REVIEW.getCode());
    }
}
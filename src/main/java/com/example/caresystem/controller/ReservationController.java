package com.example.caresystem.controller;

import com.example.caresystem.entity.Reservation;
import com.example.caresystem.service.ReservationService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/add")
    public Result<Reservation> addReservation(@RequestBody Reservation reservation,
                                             @RequestParam Integer childId,
                                             @RequestParam Integer parentId) {
        Reservation savedReservation = reservationService.addReservation(reservation, childId, parentId);
        return Result.success(savedReservation);
    }

    @GetMapping("/{id}")
    public Result<Reservation> getReservationById(@PathVariable Integer id) {
        Reservation reservation = reservationService.getReservationById(id);
        return Result.success(reservation);
    }

    @GetMapping("/list")
    public Result<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return Result.success(reservations);
    }

    @GetMapping("/child/{childId}")
    public Result<List<Reservation>> getReservationsByChild(@PathVariable Integer childId) {
        List<Reservation> reservations = reservationService.getReservationsByChild(childId);
        return Result.success(reservations);
    }

    @GetMapping("/parent/{parentId}")
    public Result<List<Reservation>> getReservationsByParent(@PathVariable Integer parentId) {
        List<Reservation> reservations = reservationService.getReservationsByParent(parentId);
        return Result.success(reservations);
    }

    @GetMapping("/date")
    public Result<List<Reservation>> getReservationsByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reserveDate) {
        List<Reservation> reservations = reservationService.getReservationsByDate(reserveDate);
        return Result.success(reservations);
    }

    @GetMapping("/status/{status}")
    public Result<List<Reservation>> getReservationsByStatus(@PathVariable String status) {
        List<Reservation> reservations = reservationService.getReservationsByStatus(status);
        return Result.success(reservations);
    }

    @GetMapping("/child/{childId}/status/{status}")
    public Result<List<Reservation>> getReservationsByChildAndStatus(@PathVariable Integer childId,
                                                                     @PathVariable String status) {
        List<Reservation> reservations = reservationService.getReservationsByChildAndStatus(childId, status);
        return Result.success(reservations);
    }

    @GetMapping("/child/{childId}/date")
    public Result<List<Reservation>> getReservationsByChildAndDate(@PathVariable Integer childId,
                                                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reserveDate) {
        List<Reservation> reservations = reservationService.getReservationsByChildAndDate(childId, reserveDate);
        return Result.success(reservations);
    }

    @PutMapping("/update/{id}")
    public Result<Reservation> updateReservation(@PathVariable Integer id,
                                                 @RequestBody Reservation reservation) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservation);
        return Result.success(updatedReservation);
    }

    @PutMapping("/audit/{id}")
    public Result<Reservation> auditReservation(@PathVariable Integer id,
                                                @RequestParam Integer auditorId,
                                                @RequestParam String status) {
        Reservation reservation = reservationService.auditReservation(id, auditorId, status);
        return Result.success(reservation);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteReservation(@PathVariable Integer id) {
        reservationService.deleteReservation(id);
        return Result.success();
    }

    @PutMapping("/cancel/{id}")
    public Result<Reservation> cancelReservation(@PathVariable Integer id) {
        Reservation reservation = reservationService.cancelReservation(id);
        return Result.success(reservation);
    }

    @GetMapping("/today")
    public Result<List<Reservation>> getTodayReservations() {
        List<Reservation> reservations = reservationService.getTodayReservations();
        return Result.success(reservations);
    }

    @GetMapping("/pending")
    public Result<List<Reservation>> getPendingReservations() {
        List<Reservation> reservations = reservationService.getPendingReservations();
        return Result.success(reservations);
    }
}
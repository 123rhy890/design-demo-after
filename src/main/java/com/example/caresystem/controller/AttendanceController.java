package com.example.caresystem.controller;

import com.example.caresystem.entity.Attendance;
import com.example.caresystem.entity.Child;
import com.example.caresystem.service.AttendanceService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/add")
    public Result<Attendance> addAttendance(@RequestBody Attendance attendance,
                                          @RequestParam Integer childId,
                                          @RequestParam Integer teacherId,
                                          @RequestParam(required = false) Integer reserveId) {
        Attendance savedAttendance = attendanceService.addAttendance(attendance, childId, teacherId, reserveId);
        return Result.success(savedAttendance);
    }

    @GetMapping("/{id:\\d+}")
    public Result<Attendance> getAttendanceById(@PathVariable Integer id) {
        Attendance attendance = attendanceService.getAttendanceById(id);
        return Result.success(attendance);
    }

    @GetMapping("/list")
    public Result<List<Attendance>> getAllAttendances() {
        List<Attendance> attendances = attendanceService.getAllAttendances();
        return Result.success(attendances);
    }

    @GetMapping("/child/{childId}")
    public Result<List<Attendance>> getAttendancesByChild(@PathVariable Integer childId) {
        List<Attendance> attendances = attendanceService.getAttendancesByChild(childId);
        return Result.success(attendances);
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<Attendance>> getAttendancesByTeacher(@PathVariable Integer teacherId) {
        List<Attendance> attendances = attendanceService.getAttendancesByTeacher(teacherId);
        return Result.success(attendances);
    }

    @GetMapping("/status/{status}")
    public Result<List<Attendance>> getAttendancesByStatus(@PathVariable Integer status) {
        List<Attendance> attendances = attendanceService.getAttendancesByStatus(status);
        return Result.success(attendances);
    }

    @GetMapping("/checkin/code")
    public Result<Attendance> getAttendanceByCheckinCode(@RequestParam String checkinCode) {
        Attendance attendance = attendanceService.getAttendanceByCheckinCode(checkinCode);
        return Result.success(attendance);
    }

    @GetMapping("/checkout/code")
    public Result<Attendance> getAttendanceByCheckoutCode(@RequestParam String checkoutCode) {
        Attendance attendance = attendanceService.getAttendanceByCheckoutCode(checkoutCode);
        return Result.success(attendance);
    }

    @PutMapping("/checkin/{id}")
    public Result<Attendance> checkin(@PathVariable Integer id, @RequestParam String checkinCode) {
        Attendance attendance = attendanceService.checkin(id, checkinCode);
        return Result.success(attendance);
    }

    @PutMapping("/checkout/{id}")
    public Result<Attendance> checkout(@PathVariable Integer id,
                                      @RequestParam String checkoutCode,
                                      @RequestParam(required = false) String pickPerson,
                                      @RequestParam(required = false) String pickPhone) {
        Attendance attendance = attendanceService.checkout(id, checkoutCode, pickPerson, pickPhone);
        return Result.success(attendance);
    }

    @PostMapping("/checkin")
    public Result<Attendance> quickCheckin(@RequestParam Integer childId,
                                         @RequestParam Integer teacherId,
                                         @RequestParam String checkinCode,
                                         @RequestParam(required = false) String remark) {
        Attendance attendance = attendanceService.quickCheckin(childId, teacherId, checkinCode, remark);
        return Result.success(attendance);
    }

    @PostMapping("/checkout")
    public Result<Attendance> quickCheckout(@RequestParam Integer childId,
                                          @RequestParam Integer teacherId,
                                          @RequestParam String checkoutCode,
                                          @RequestParam String pickPerson,
                                          @RequestParam String pickPhone,
                                          @RequestParam(required = false) String remark) {
        Attendance attendance = attendanceService.quickCheckout(childId, teacherId, checkoutCode, pickPerson, pickPhone, remark);
        return Result.success(attendance);
    }

    @GetMapping("/absent")
    public Result<List<Child>> getTodayAbsentChildren() {
        List<Child> children = attendanceService.getTodayAbsentChildren();
        return Result.success(children);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteAttendance(@PathVariable Integer id) {
        attendanceService.deleteAttendance(id);
        return Result.success();
    }

    @GetMapping("/today")
    public Result<List<Attendance>> getTodayAttendances() {
        List<Attendance> attendances = attendanceService.getTodayAttendances();
        return Result.success(attendances);
    }

    @PostMapping("/manual/checkin")
    public Result<Attendance> manualCheckin(@RequestParam Integer reserveId, @RequestParam String signCode) {
        Attendance attendance = attendanceService.manualCheckin(reserveId, signCode);
        return Result.success(attendance);
    }

    @GetMapping("/child/{childId}/checkinDates")
    public Result<List<LocalDate>> getCheckinDatesByChildAndMonth(@PathVariable Integer childId,
                                                                  @RequestParam String month) {
        List<LocalDate> dates = attendanceService.getCheckinDatesByChildAndMonth(childId, month);
        return Result.success(dates);
    }
}
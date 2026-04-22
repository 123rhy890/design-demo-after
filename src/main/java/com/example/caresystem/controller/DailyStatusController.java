package com.example.caresystem.controller;

import com.example.caresystem.entity.DailyStatus;
import com.example.caresystem.service.DailyStatusService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dailyStatus")
public class DailyStatusController {

    @Autowired
    private DailyStatusService dailyStatusService;

    @PostMapping("/add")
    public Result<DailyStatus> addDailyStatus(@RequestBody DailyStatus dailyStatus,
                                            @RequestParam Integer childId,
                                            @RequestParam Integer teacherId) {
        DailyStatus savedStatus = dailyStatusService.addDailyStatus(dailyStatus, childId, teacherId);
        return Result.success(savedStatus);
    }

    @GetMapping("/{id}")
    public Result<DailyStatus> getDailyStatusById(@PathVariable Integer id) {
        DailyStatus dailyStatus = dailyStatusService.getDailyStatusById(id);
        return Result.success(dailyStatus);
    }

    @GetMapping("/list")
    public Result<List<DailyStatus>> getAllDailyStatuses() {
        List<DailyStatus> statuses = dailyStatusService.getAllDailyStatuses();
        return Result.success(statuses);
    }

    @GetMapping("/child/{childId}")
    public Result<List<DailyStatus>> getDailyStatusesByChild(@PathVariable Integer childId) {
        List<DailyStatus> statuses = dailyStatusService.getDailyStatusesByChild(childId);
        return Result.success(statuses);
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<DailyStatus>> getDailyStatusesByTeacher(@PathVariable Integer teacherId) {
        List<DailyStatus> statuses = dailyStatusService.getDailyStatusesByTeacher(teacherId);
        return Result.success(statuses);
    }

    @GetMapping("/date")
    public Result<List<DailyStatus>> getDailyStatusesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate) {
        List<DailyStatus> statuses = dailyStatusService.getDailyStatusesByDate(recordDate);
        return Result.success(statuses);
    }

    @GetMapping("/child/{childId}/date")
    public Result<DailyStatus> getDailyStatusByChildAndDate(@PathVariable Integer childId,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate recordDate) {
        DailyStatus status = dailyStatusService.getDailyStatusByChildAndDate(childId, recordDate);
        return Result.success(status);
    }

    @GetMapping("/today")
    public Result<List<DailyStatus>> getTodayDailyStatuses() {
        List<DailyStatus> statuses = dailyStatusService.getTodayDailyStatuses();
        return Result.success(statuses);
    }

    @GetMapping("/abnormal")
    public Result<List<DailyStatus>> getAbnormalRecords() {
        List<DailyStatus> statuses = dailyStatusService.getAbnormalRecords();
        return Result.success(statuses);
    }

    @PutMapping("/update/{id}")
    public Result<DailyStatus> updateDailyStatus(@PathVariable Integer id,
                                                 @RequestBody DailyStatus dailyStatus) {
        DailyStatus updatedStatus = dailyStatusService.updateDailyStatus(id, dailyStatus);
        return Result.success(updatedStatus);
    }

    @PutMapping("/push/{id}")
    public Result<DailyStatus> pushToParent(@PathVariable Integer id) {
        DailyStatus status = dailyStatusService.pushToParent(id);
        return Result.success(status);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDailyStatus(@PathVariable Integer id) {
        dailyStatusService.deleteDailyStatus(id);
        return Result.success();
    }

    @GetMapping("/child/{childId}/range")
    public Result<List<DailyStatus>> getDailyStatusesByDateRange(@PathVariable Integer childId,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DailyStatus> statuses = dailyStatusService.getDailyStatusesByDateRange(childId, startDate, endDate);
        return Result.success(statuses);
    }
}
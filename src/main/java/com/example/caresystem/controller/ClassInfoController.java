package com.example.caresystem.controller;

import com.example.caresystem.entity.ClassInfo;
import com.example.caresystem.service.ClassInfoService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassInfoController {

    @Autowired
    private ClassInfoService classInfoService;

    @GetMapping("/stats")
    public Result<List<Map<String, Object>>> getAllClassesWithStats() {
        return Result.success(classInfoService.getAllClassesWithStats());
    }

    @PostMapping("/add")
    public Result<ClassInfo> addClass(@RequestBody ClassInfo classInfo,
                                      @RequestParam Integer teacherId) {
        ClassInfo savedClass = classInfoService.addClass(classInfo, teacherId);
        return Result.success(savedClass);
    }

    @GetMapping("/{id:\\d+}")
    public Result<ClassInfo> getClassById(@PathVariable Integer id) {
        ClassInfo classInfo = classInfoService.getClassById(id);
        return Result.success(classInfo);
    }

    @GetMapping("/list")
    public Result<List<ClassInfo>> getAllClasses() {
        List<ClassInfo> classes = classInfoService.getAllClasses();
        return Result.success(classes);
    }

    @GetMapping("/normal")
    public Result<List<ClassInfo>> getNormalClasses() {
        List<ClassInfo> classes = classInfoService.getNormalClasses();
        return Result.success(classes);
    }

    @GetMapping("/teacher/{teacherId}")
    public Result<List<ClassInfo>> getClassesByTeacher(@PathVariable Integer teacherId) {
        List<ClassInfo> classes = classInfoService.getClassesByTeacher(teacherId);
        return Result.success(classes);
    }

    @GetMapping("/status/{status}")
    public Result<List<ClassInfo>> getClassesByStatus(@PathVariable Integer status) {
        List<ClassInfo> classes = classInfoService.getClassesByStatus(status);
        return Result.success(classes);
    }

    @PutMapping("/update/{id}")
    public Result<ClassInfo> updateClass(@PathVariable Integer id,
                                         @RequestBody ClassInfo classInfo,
                                         @RequestParam(required = false) Integer teacherId) {
        ClassInfo updatedClass = classInfoService.updateClass(id, classInfo, teacherId);
        return Result.success(updatedClass);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteClass(@PathVariable Integer id) {
        classInfoService.deleteClass(id);
        return Result.success();
    }

    @PutMapping("/status/{id}")
    public Result<ClassInfo> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        ClassInfo classInfo = classInfoService.updateStatus(id, status);
        return Result.success(classInfo);
    }

    @GetMapping("/count/teacher/{teacherId}")
    public Result<Long> countByTeacher(@PathVariable Integer teacherId) {
        Long count = classInfoService.countByTeacher(teacherId);
        return Result.success(count);
    }

    @GetMapping("/count/status/{status}")
    public Result<Long> countByStatus(@PathVariable Integer status) {
        Long count = classInfoService.countByStatus(status);
        return Result.success(count);
    }

    @GetMapping("/search")
    public Result<List<ClassInfo>> searchClasses(@RequestParam String keyword) {
        List<ClassInfo> classes = classInfoService.searchClasses(keyword);
        return Result.success(classes);
    }
}
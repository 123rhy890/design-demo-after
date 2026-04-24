package com.example.caresystem.controller;

import com.example.caresystem.entity.Child;
import com.example.caresystem.service.ChildService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/child")
public class ChildController {

    @Autowired
    private ChildService childService;

    @PostMapping("/add")
    public Result<Child> addChild(@RequestBody Child child,
                                  @RequestParam Integer parentId,
                                  @RequestParam(required = false) Integer classId) {
        Child savedChild = childService.addChild(child, parentId, classId);
        return Result.success(savedChild);
    }

    @GetMapping("/parent/{parentId}")
    public Result<List<Child>> getChildListByParent(@PathVariable Integer parentId) {
        List<Child> childList = childService.getChildListByParentId(parentId);
        return Result.success(childList);
    }

    @GetMapping("/{id:\\d+}")
    public Result<Child> getChildById(@PathVariable Integer id) {
        Child child = childService.getChildById(id);
        return Result.success(child);
    }

    @PutMapping("/update/{id}")
    public Result<Child> updateChild(@PathVariable Integer id,
                                     @RequestBody Child child,
                                     @RequestParam(required = false) Integer classId) {
        Child updatedChild = childService.updateChild(id, child, classId);
        return Result.success(updatedChild);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteChild(@PathVariable Integer id) {
        childService.deleteChild(id);
        return Result.success();
    }

    @GetMapping("/search")
    public Result<List<Child>> searchChild(@RequestParam String keyword) {
        List<Child> childList = childService.searchChild(keyword);
        return Result.success(childList);
    }

    @GetMapping("/class/{classId}")
    public Result<List<Child>> getChildrenByClass(@PathVariable Integer classId) {
        List<Child> childList = childService.getChildrenByClassId(classId);
        return Result.success(childList);
    }

    @GetMapping("/list")
    public Result<List<Child>> getAllChildren() {
        List<Child> childList = childService.getAllChildren();
        return Result.success(childList);
    }

    @PutMapping("/class/update/{childId}")
    public Result<Child> updateChildClass(@PathVariable Integer childId,
                                          @RequestParam Integer classId) {
        Child updatedChild = childService.updateClassInfo(childId, classId);
        return Result.success(updatedChild);
    }

    @PutMapping("/audit/{id}")
    public Result<Child> auditChild(@PathVariable Integer id,
                                    @RequestParam Integer status,
                                    @RequestParam String auditorName) {
        Child child = childService.getChildById(id);
        child.setStatus(status);
        child.setAuditorName(auditorName);
        child.setAuditTime(java.time.LocalDateTime.now());
        childService.updateChild(id, child, null);
        return Result.success(child);
    }

    @GetMapping("/pending")
    public Result<List<Child>> getPendingChildren() {
        List<Child> childList = childService.getAllChildren().stream()
                .filter(c -> c.getStatus() == 0)
                .collect(java.util.stream.Collectors.toList());
        return Result.success(childList);
    }
}
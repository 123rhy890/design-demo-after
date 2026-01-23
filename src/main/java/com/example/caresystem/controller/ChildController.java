package com.example.caresystem.controller;

import com.example.caresystem.entity.Child;
import com.example.caresystem.service.ChildService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 儿童信息接口层 - 对外提供RESTful接口
 * 接口前缀：http://localhost:8080/api/child
 * 所有接口返回统一Result格式，前端解析无压力
 */
@RestController
@RequestMapping("/child")
public class ChildController {

    @Autowired
    private ChildService childService;

    /**
     * 新增儿童信息
     * POST 请求：http://localhost:8080/api/child/add
     * 请求参数：child实体+parentId(家长ID)+classId(班级ID)
     */
    @PostMapping("/add")
    public Result<Child> addChild(@RequestBody Child child,
                                  @RequestParam Long parentId,
                                  @RequestParam Long classId) {
        Child savedChild = childService.addChild(child, parentId, classId);
        return Result.success(savedChild);
    }

    /**
     * 根据家长ID查询孩子列表
     * GET 请求：http://localhost:8080/api/child/parent/{parentId}
     */
    @GetMapping("/parent/{parentId}")
    public Result<List<Child>> getChildListByParent(@PathVariable Long parentId) {
        List<Child> childList = childService.getChildListByParentId(parentId);
        return Result.success(childList);
    }

    /**
     * 根据儿童ID查询详情
     * GET 请求：http://localhost:8080/api/child/{id}
     */
    @GetMapping("/{id}")
    public Result<Child> getChildById(@PathVariable Long id) {
        Child child = childService.getChildById(id);
        return Result.success(child);
    }

    /**123*/
    /**
     * 修改儿童信息111
     * PUT 请求：http://localhost:8080/api/child/update/{id}
     */
    @PutMapping("/update/{id}")
    public Result<Child> updateChild(@PathVariable Long id,
                                     @RequestBody Child child,
                                     @RequestParam(required = false) Long classId) {
        Child updatedChild = childService.updateChild(id, child, classId);
        return Result.success(updatedChild);
    }

    /**
     * 删除儿童信息
     * DELETE 请求：http://localhost:8080/api/child/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteChild(@PathVariable Long id) {
        childService.deleteChild(id);
        return Result.success();
    }

    /**
     * 模糊搜索儿童（按姓名）
     * GET 请求：http://localhost:8080/api/child/search?name=小明
     */
    @GetMapping("/search")
    public Result<List<Child>> searchChild(@RequestParam String name) {
        List<Child> childList = childService.searchChild(name);
        return Result.success(childList);
    }
}
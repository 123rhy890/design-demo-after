package com.example.caresystem.controller;

import com.example.caresystem.entity.Allergy;
import com.example.caresystem.service.AllergyService;
import com.example.caresystem.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allergy")
public class AllergyController {

    @Autowired
    private AllergyService allergyService;

    @PostMapping("/add")
    public Result<Allergy> addAllergy(@RequestBody Allergy allergy, @RequestParam Integer childId) {
        Allergy savedAllergy = allergyService.addAllergy(allergy, childId);
        return Result.success(savedAllergy);
    }

    @GetMapping("/{id}")
    public Result<Allergy> getAllergyById(@PathVariable Integer id) {
        Allergy allergy = allergyService.getAllergyById(id);
        return Result.success(allergy);
    }

    @GetMapping("/list")
    public Result<List<Allergy>> getAllAllergies() {
        List<Allergy> allergies = allergyService.getAllAllergies();
        return Result.success(allergies);
    }

    @GetMapping("/child/{childId}")
    public Result<List<Allergy>> getAllergiesByChild(@PathVariable Integer childId) {
        List<Allergy> allergies = allergyService.getAllergiesByChild(childId);
        return Result.success(allergies);
    }

    @GetMapping("/type/{allergyType}")
    public Result<List<Allergy>> getAllergiesByType(@PathVariable String allergyType) {
        List<Allergy> allergies = allergyService.getAllergiesByType(allergyType);
        return Result.success(allergies);
    }

    @GetMapping("/active")
    public Result<List<Allergy>> getActiveAllergies() {
        List<Allergy> allergies = allergyService.getActiveAllergies();
        return Result.success(allergies);
    }

    @GetMapping("/active/child/{childId}")
    public Result<List<Allergy>> getActiveAllergiesByChild(@PathVariable Integer childId) {
        List<Allergy> allergies = allergyService.getActiveAllergiesByChild(childId);
        return Result.success(allergies);
    }

    @PutMapping("/update/{id}")
    public Result<Allergy> updateAllergy(@PathVariable Integer id, @RequestBody Allergy allergy) {
        Allergy updatedAllergy = allergyService.updateAllergy(id, allergy);
        return Result.success(updatedAllergy);
    }

    @PutMapping("/status/{id}")
    public Result<Allergy> updateStatus(@PathVariable Integer id, @RequestParam Integer status) {
        Allergy allergy = allergyService.updateStatus(id, status);
        return Result.success(allergy);
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteAllergy(@PathVariable Integer id) {
        allergyService.deleteAllergy(id);
        return Result.success();
    }

    @GetMapping("/count/child/{childId}")
    public Result<Long> countByChild(@PathVariable Integer childId) {
        Long count = allergyService.countByChild(childId);
        return Result.success(count);
    }
}
package com.devgraph.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devgraph.dto.ApiResponse;
import com.devgraph.model.Company;
import com.devgraph.model.Developer;
import com.devgraph.model.Skill;
import com.devgraph.service.DeveloperService;

import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/developers")
@Validated
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @GetMapping
    public ApiResponse<List<Developer>> getAllDevelopers() {
        return ApiResponse.success(developerService.getAllDevelopers());
    }

    @GetMapping("/{developerId}")
    public ApiResponse<Developer> getDeveloper(@PathVariable @NotBlank String developerId) {
        return ApiResponse.success(developerService.getDeveloper(developerId));
    }

    @GetMapping("/{developerId}/companies")
    public ApiResponse<List<Company>> getDeveloperCompanies(@PathVariable @NotBlank String developerId) {
        return ApiResponse.success(developerService.getDeveloperCompanies(developerId));
    }

    @GetMapping("/{developerId}/skills")
    public ApiResponse<List<Skill>> getDeveloperSkills(@PathVariable @NotBlank String developerId) {
        return ApiResponse.success(developerService.getDeveloperSkills(developerId));
    }
}

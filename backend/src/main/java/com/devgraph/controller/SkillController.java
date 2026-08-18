package com.devgraph.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devgraph.dto.ApiResponse;
import com.devgraph.model.Skill;
import com.devgraph.service.SkillService;

@RestController
@RequestMapping("/api/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping
    public ApiResponse<List<Skill>> getAllSkills() {
        return ApiResponse.success(skillService.getAllSkills());
    }
}

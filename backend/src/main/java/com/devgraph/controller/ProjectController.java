package com.devgraph.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devgraph.dto.ApiResponse;
import com.devgraph.model.Project;
import com.devgraph.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public ApiResponse<List<Project>> getProjects(@RequestParam(required = false) String developerId) {
        return ApiResponse.success(projectService.getProjects(developerId));
    }
}

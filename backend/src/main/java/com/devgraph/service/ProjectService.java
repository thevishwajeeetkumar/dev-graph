package com.devgraph.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Project;
import com.devgraph.repository.ProjectRepository;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getProjects(String developerId) {
        if (!StringUtils.hasText(developerId)) {
            return projectRepository.findAll();
        }
        if (!projectRepository.developerExists(developerId)) {
            throw new ResourceNotFoundException("Developer not found: " + developerId);
        }
        return projectRepository.findByDeveloperId(developerId);
    }
}

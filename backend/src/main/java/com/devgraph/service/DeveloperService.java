package com.devgraph.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgraph.exception.ResourceNotFoundException;
import com.devgraph.model.Company;
import com.devgraph.model.Developer;
import com.devgraph.model.Skill;
import com.devgraph.repository.DeveloperRepository;

@Service
public class DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    public Developer getDeveloper(String developerId) {
        RequestValidation.requireText(developerId, "developerId");
        return developerRepository.findById(developerId)
                .orElseThrow(() -> new ResourceNotFoundException("Developer not found: " + developerId));
    }

    public List<Company> getDeveloperCompanies(String developerId) {
        RequestValidation.requireText(developerId, "developerId");
        requireDeveloperExists(developerId);
        return developerRepository.findCompaniesByDeveloperId(developerId);
    }

    public List<Skill> getDeveloperSkills(String developerId) {
        RequestValidation.requireText(developerId, "developerId");
        requireDeveloperExists(developerId);
        return developerRepository.findSkillsByDeveloperId(developerId);
    }

    private void requireDeveloperExists(String developerId) {
        if (!developerRepository.exists(developerId)) {
            throw new ResourceNotFoundException("Developer not found: " + developerId);
        }
    }
}

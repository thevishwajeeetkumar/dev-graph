package com.devgraph.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devgraph.dto.ApiResponse;
import com.devgraph.model.Company;
import com.devgraph.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ApiResponse<List<Company>> getAllCompanies() {
        return ApiResponse.success(companyService.getAllCompanies());
    }
}

package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/about")
public class CompanyController {
    private final CompanyService companyService;
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/company")
    public void addCompany(@RequestBody CompanyEntity companyEntity){
        companyService.addNewCompany(companyEntity);
    }
}

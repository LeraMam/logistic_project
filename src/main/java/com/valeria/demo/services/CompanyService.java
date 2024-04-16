package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    @Autowired
    public CompanyService(CompanyRepository companyRepository, TariffRepository tariffRepository) {
        this.companyRepository = companyRepository;
    }
    public CompanyEntity findCompanyById(Long id){
        return companyRepository.findCompanyEntityById(id);
    }
    public boolean isCompanyExists(Long id){
        return companyRepository.existsCompanyEntityById(id);
    }
    public List<CompanyEntity> getAllCompanies(){
        return companyRepository.findAll();
    }
    public CompanyEntity addNewCompany(CompanyEntity companyEntity){
        return companyRepository.save(companyEntity);
    }

}

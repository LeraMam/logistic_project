package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffService {
    private final TariffRepository tariffRepository;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    @Autowired
    public TariffService(TariffRepository tariffRepository, CompanyService companyService, CompanyRepository companyRepository) {
        this.tariffRepository = tariffRepository;
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }
    public void addNewTariff(Long companyId, TariffEntity tariffEntity){
        TariffEntity savedEntity = tariffRepository.save(tariffEntity);
        CompanyEntity companyEntity = companyService.findCompanyById(companyId);
        List<TariffEntity> tariffList = companyEntity.getTariff();
        tariffList.add(savedEntity);
        companyEntity.setTariff(tariffList);
        companyRepository.save(companyEntity);
    }
}

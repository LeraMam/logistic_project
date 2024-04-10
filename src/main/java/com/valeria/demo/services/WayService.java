package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.WayEntity;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.WayRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class WayService {
    private final WayRepository wayRepository;
    private final CompanyRepository companyRepository;

    public WayService(WayRepository wayRepository, CompanyRepository companyRepository) {
        this.wayRepository = wayRepository;
        this.companyRepository = companyRepository;
    }

     public List<WayEntity> getAllWaysForCompany(Long companyId){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        if(companyEntity!=null){
            return companyEntity.getWays();
        }
        else return Collections.emptyList();
    }

    public void addNewWay(Long companyId, WayEntity wayEntity){
        WayEntity savedEntity = wayRepository.save(wayEntity);
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        List<WayEntity> waysList = companyEntity.getWays();
        waysList.add(savedEntity);
        companyEntity.setWays(waysList);
        companyRepository.save(companyEntity);
    }
}

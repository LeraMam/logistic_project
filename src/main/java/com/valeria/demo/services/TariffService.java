package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.TariffRepository;
import com.valeria.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TariffService {
    private final TariffRepository tariffRepository;
    private final CompanyRepository companyRepository;
    @Autowired
    public TariffService(TariffRepository tariffRepository, CompanyRepository companyRepository) {
        this.tariffRepository = tariffRepository;
        this.companyRepository = companyRepository;
    }

    public List<TariffEntity> getAllTariffsForCompany(Long companyId){
        return companyRepository.findCompanyEntityById(companyId).getTariff();
    }

    public List<TariffEntity> getAllTariffs(){
        return tariffRepository.findAll();
    }

    public TariffEntity findTariffEntityById(Long id){
        return tariffRepository.findTariffEntityById(id);
    }

    public boolean isTariffExists(Long id){
        return tariffRepository.existsTariffEntityById(id);
    }

    public void addNewTariff(Long companyId, TariffEntity tariffEntity){
        TariffEntity savedEntity = tariffRepository.save(tariffEntity);
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        List<TariffEntity> tariffList = companyEntity.getTariff();
        tariffList.add(savedEntity);
        companyEntity.setTariff(tariffList);
        companyRepository.save(companyEntity);
    }

    public TariffEntity editTariff(Long companyId, Long tariffId, TariffEntity tariffEntity){
        TariffEntity savedEntity = new TariffEntity();
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        List<TariffEntity> tariffList = companyEntity.getTariff();
        for(int i = 0; i < tariffList.size(); i++){
            TariffEntity existingTariff = tariffList.get(i);
            if (existingTariff.getId().equals(tariffId)) {
                /*System.out.println("Done!");*/
                tariffEntity.setId(tariffId);
                System.out.println(tariffEntity);
                savedEntity = tariffRepository.save(tariffEntity);
                System.out.println(savedEntity);
                tariffList.set(i, savedEntity);
                companyEntity.setTariff(tariffList);
                companyRepository.save(companyEntity);
                break;
            }
        }
        return savedEntity;
    }

    public void deleteTariff(Long id){
        boolean exists = isTariffExists(id);
        if (!exists) {
            throw new NotFoundException("Тариф с заданным id не существует");
        }
        tariffRepository.deleteById(id);
    }
}

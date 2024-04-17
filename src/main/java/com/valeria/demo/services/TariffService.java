package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.IntervalWayEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.entity.WayEntity;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.IntervalWayRepository;
import com.valeria.demo.db.repositories.TariffRepository;
import com.valeria.demo.db.repositories.WayRepository;
import com.valeria.demo.exception.BadRequestException;
import com.valeria.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TariffService {
    private final TariffRepository tariffRepository;
    private final CompanyRepository companyRepository;
    private final IntervalWayRepository intervalWayRepository;
    @Autowired
    public TariffService(TariffRepository tariffRepository, CompanyRepository companyRepository,
                         IntervalWayRepository intervalWayRepository) {
        this.tariffRepository = tariffRepository;
        this.companyRepository = companyRepository;
        this.intervalWayRepository = intervalWayRepository;
    }

    public List<TariffEntity> getAllTariffsForCompany(Long companyId){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        if(companyEntity!=null){
            return companyEntity.getTariffs();
        }
        else return Collections.emptyList();
    }

    public List<TariffEntity> getAllTariffsForIntervalWay(Long intervalWayId){
        IntervalWayEntity intervalWayEntity = intervalWayRepository.findIntervalWayEntityById(intervalWayId);
        if(intervalWayEntity != null){
            return intervalWayEntity.getTariffs();
        }
        else return Collections.emptyList();
    }

    public List<TariffEntity> getAllTariffs(){
        return tariffRepository.findAll();
    }

    public boolean isTariffExists(Long id){
        return tariffRepository.existsTariffEntityById(id);
    }

    public void addNewTariff(Long companyId, TariffEntity tariffEntity){
        TariffEntity savedEntity = tariffRepository.save(tariffEntity);
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        List<TariffEntity> tariffList = companyEntity.getTariffs();
        tariffList.add(savedEntity);
        companyEntity.setTariffs(tariffList);
        companyRepository.save(companyEntity);
    }
    public TariffEntity editTariff(Long companyId, Long tariffId, TariffEntity tariffEntity){
        TariffEntity savedEntity = new TariffEntity();
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        List<TariffEntity> tariffList = companyEntity.getTariffs();
        for(int i = 0; i < tariffList.size(); i++){
            TariffEntity existingTariff = tariffList.get(i);
            if (existingTariff.getId().equals(tariffId)) {
                System.out.println("Done!");
                tariffEntity.setId(tariffId);
                System.out.println(tariffEntity);
                savedEntity = tariffRepository.save(tariffEntity);
                System.out.println(savedEntity);
                tariffList.set(i, savedEntity);
                companyEntity.setTariffs(tariffList);
                companyRepository.save(companyEntity);
                break;
            }
        }
        return savedEntity;
    }

    public void deleteTariff(Long tariffId, Long companyId){
        if (!isTariffExists(tariffId)) throw new NotFoundException("Тариф с заданным id не существует");
        if (!companyRepository.existsCompanyEntityById(companyId)) throw new NotFoundException("Компания с заданным id не существует");
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        for(WayEntity wayEntity : companyEntity.getWays()){
            for(IntervalWayEntity intervalWayEntity : wayEntity.getIntervalWays()){
                for(TariffEntity tariffEntity : intervalWayEntity.getTariffs()){
                    if(Objects.equals(tariffEntity.getId(), tariffId)){
                        throw new BadRequestException("Тариф используются в путях, его нельзя удалить");
                    }
                }
            }
        }
        List<TariffEntity> tariffEntityList = companyEntity.getTariffs();
        for(int i=0; i < tariffEntityList.size(); i++){
            if(Objects.equals(tariffEntityList.get(i).getId(), tariffId)){
                tariffEntityList.remove(i);
            }
        }
        companyEntity.setTariffs(tariffEntityList);
        companyRepository.save(companyEntity);
        tariffRepository.deleteById(tariffId);
    }
}

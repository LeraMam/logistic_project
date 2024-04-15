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
    private final WayRepository wayRepository;
    private final IntervalWayRepository intervalWayRepository;
    @Autowired
    public TariffService(TariffRepository tariffRepository, CompanyRepository companyRepository, WayRepository wayRepository, IntervalWayRepository intervalWayRepository) {
        this.tariffRepository = tariffRepository;
        this.companyRepository = companyRepository;
        this.wayRepository = wayRepository;
        this.intervalWayRepository = intervalWayRepository;
    }

    public List<TariffEntity> getAllTariffsForCompany(Long companyId){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        if(companyEntity!=null){
            return companyEntity.getTariffs();
        }
        else return Collections.emptyList();
    }

    public List<TariffEntity> getAllTariffsForWay(Long wayId){
        WayEntity wayEntity = wayRepository.findWayEntityById(wayId);
        if(wayEntity!=null){
            List<IntervalWayEntity> intervalWayEntityList = wayEntity.getIntervalWays();
            List<TariffEntity> tariffEntityList = new ArrayList<>();
            for(IntervalWayEntity intervalWayEntity : intervalWayEntityList){
                List<TariffEntity> newTariffEntityList = intervalWayEntity.getTariffs();
                tariffEntityList.addAll(newTariffEntityList);
            }
            return tariffEntityList;
        }
        else return Collections.emptyList();
    }

    public List<TariffEntity> getAllTariffsForIntervalWay(Long intervalWayId){
        IntervalWayEntity intervalWayEntity = intervalWayRepository.findIntervalWayEntityById(intervalWayId);
        if(intervalWayEntity != null){
            List<TariffEntity> tariffEntityList = intervalWayEntity.getTariffs();
            return tariffEntityList;
        }
        else return Collections.emptyList();
    }

    /*public List<TariffEntity> getAllTariffsForCompany(Long companyId){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        List<WayEntity> wayEntityList = new ArrayList<>();
        if(companyEntity!=null){
            wayEntityList = companyEntity.getWays();
        }
        Set<TariffEntity> uniqueTariffEntities = new HashSet<>();
        for (WayEntity wayEntity : wayEntityList) {
            List<IntervalWayEntity> intervalWayEntityList = wayEntity.getIntervalWays();

            for (IntervalWayEntity intervalWayEntity : intervalWayEntityList) {
                List<TariffEntity> tariffEntityList = intervalWayEntity.getTariffs();
                uniqueTariffEntities.addAll(tariffEntityList);
            }
        }
        List<TariffEntity> resultTariffEntityList = new ArrayList<>(uniqueTariffEntities);
        System.out.println(resultTariffEntityList);
        return resultTariffEntityList;
    }*/

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
                    if(tariffEntity.getId() == tariffId){
                        throw new BadRequestException("Тариф используются в путях, его нельзя удалить");
                    }
                }
            }
        }
        List<TariffEntity> tariffEntityList = companyEntity.getTariffs();
        for(int i=0; i < tariffEntityList.size(); i++){
            if(tariffEntityList.get(i).getId() == tariffId){
                tariffEntityList.remove(i);
            }
        }
        companyEntity.setTariffs(tariffEntityList);
        companyRepository.save(companyEntity);
        tariffRepository.deleteById(tariffId);
    }
}

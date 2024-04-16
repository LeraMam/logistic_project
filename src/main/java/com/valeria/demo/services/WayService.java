package com.valeria.demo.services;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.IntervalWayEntity;
import com.valeria.demo.db.entity.OrderEntity;
import com.valeria.demo.db.entity.WayEntity;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.OrderRepository;
import com.valeria.demo.db.repositories.WayRepository;
import com.valeria.demo.exception.BadRequestException;
import com.valeria.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WayService {
    private final WayRepository wayRepository;
    private final CompanyRepository companyRepository;

    private final OrderRepository orderRepository;

    public WayService(WayRepository wayRepository, CompanyRepository companyRepository, OrderRepository orderRepository) {
        this.wayRepository = wayRepository;
        this.companyRepository = companyRepository;
        this.orderRepository = orderRepository;
    }

     public List<WayEntity> getAllWaysForCompany(Long companyId){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        if(companyEntity!=null){
            return companyEntity.getWays();
        }
        else return Collections.emptyList();
    }

    public void addNewWay(Long companyId, WayEntity wayEntity){
        if(wayEntity != null) {
            WayEntity savedEntity = wayRepository.save(wayEntity);
            CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
            List<WayEntity> waysList = companyEntity.getWays();
            waysList.add(savedEntity);
            companyEntity.setWays(waysList);
            companyRepository.save(companyEntity);
        }
    }

    public void editWay(Long companyId, Long wayId, WayEntity wayEntity){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        if(wayEntity != null && companyEntity != null) {
            WayEntity changedEntity = wayRepository.findWayEntityById(wayId);
            List<OrderEntity> orderEntityList = orderRepository.findAll();
            for(OrderEntity order : orderEntityList){
                if(order.getWay().getId() == changedEntity.getId()) throw new BadRequestException("Путь используется в заказах, его нельзя изменить");
            }
            changedEntity.setStartPoint(wayEntity.getStartPoint());
            changedEntity.setEndPoint(wayEntity.getEndPoint());
            changedEntity.setPointNumber(wayEntity.getPointNumber());
            List<IntervalWayEntity> freeList = new ArrayList<>();
            changedEntity.setIntervalWays(freeList);
            WayEntity savedEntity = wayRepository.save(changedEntity);
            List<WayEntity> waysList = companyEntity.getWays();
            for(int i = 0; i < waysList.size(); i++){
                if(waysList.get(i).getId() == changedEntity.getId()) waysList.remove(i);
            }
            waysList.add(changedEntity);
            companyEntity.setWays(waysList);
            companyRepository.save(companyEntity);
        }
    }

    public void deleteWay(Long companyId, Long wayId){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        WayEntity deleteEntity = wayRepository.findWayEntityById(wayId);
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        for(OrderEntity order : orderEntityList){
            if(order.getWay().getId() == deleteEntity.getId()) throw new BadRequestException("Путь используется в заказах, его нельзя удалить");
        }
        List<IntervalWayEntity> emptyList = new ArrayList<>();
        deleteEntity.setIntervalWays(emptyList);
        List<WayEntity> wayEntityList = companyEntity.getWays();
        for(int i=0; i < wayEntityList.size(); i++){
            if(wayEntityList.get(i).getId() == deleteEntity.getId()){
                wayEntityList.remove(i);
            }
        }
        companyEntity.setWays(wayEntityList);
        companyRepository.save(companyEntity);
        wayRepository.deleteById(deleteEntity.getId());
    }
}

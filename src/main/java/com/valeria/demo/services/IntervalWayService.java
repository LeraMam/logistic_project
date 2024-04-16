package com.valeria.demo.services;

import com.valeria.demo.db.entity.*;
import com.valeria.demo.db.repositories.*;
import com.valeria.demo.exception.BadRequestException;
import com.valeria.demo.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntervalWayService {
    private final CompanyRepository companyRepository;
    private final IntervalWayRepository intervalWayRepository;
    private final WayRepository wayRepository;
    private final TariffRepository tariffRepository;
    private final OrderRepository orderRepository;

    public IntervalWayService(CompanyRepository companyRepository, IntervalWayRepository intervalWayRepository, WayRepository wayRepository, TariffRepository tariffRepository, OrderRepository orderRepository) {
        this.companyRepository = companyRepository;
        this.intervalWayRepository = intervalWayRepository;
        this.wayRepository = wayRepository;
        this.tariffRepository = tariffRepository;
        this.orderRepository = orderRepository;
    }

    public void addNewIntervalWay(Long wayId, ArrayList<Long> tariffsId){
        WayEntity wayEntity = wayRepository.findWayEntityById(wayId);

        List<TariffEntity> tariffs = new ArrayList<>();
        for(Long tariffId : tariffsId){
            TariffEntity tariff = tariffRepository.findTariffEntityById(tariffId);
            tariffs.add(tariff);
        }
        IntervalWayEntity intervalWayEntity = new IntervalWayEntity();
        intervalWayEntity.setTariffs(tariffs);
        Double sumDistance = tariffs.stream().collect(Collectors.summingDouble(TariffEntity::getDistance));
        intervalWayEntity.setSumDistance(sumDistance);

        Double sumTime = tariffs.stream().collect(Collectors.summingDouble(TariffEntity::getTime));
        intervalWayEntity.setSumTime(sumTime);

        Double sumPrice = tariffs
                .stream()
                .mapToDouble(tariff -> tariff.getPrice() * tariff.getDistance())
                .sum();
        intervalWayEntity.setSumPrice(sumPrice);

        Double minMaxWeight = tariffs.stream()
                .mapToDouble(TariffEntity::getMaxWeight)
                .min()
                .orElseThrow();
        intervalWayEntity.setMaxWeight(minMaxWeight);

        intervalWayRepository.save(intervalWayEntity);

        List<IntervalWayEntity> intervalWayEntityList = wayEntity.getIntervalWays();
        intervalWayEntityList.add(intervalWayEntity);

        wayEntity.setIntervalWays(intervalWayEntityList);
        wayRepository.save(wayEntity);
    }

    public void editIntervalWay(Long wayId, Long intervalWayId, ArrayList<Long> tariffsId){
        WayEntity wayEntity = wayRepository.findWayEntityById(wayId);
        if (wayEntity == null) throw new NotFoundException("Компания с заданным id не найдена");
        IntervalWayEntity intervalWayEntity = intervalWayRepository.findIntervalWayEntityById(intervalWayId);

        List<TariffEntity> tariffs = new ArrayList<>();
        for(Long tariffId : tariffsId){
            TariffEntity tariff = tariffRepository.findTariffEntityById(tariffId);
            tariffs.add(tariff);
        }
        intervalWayEntity.setId(intervalWayId);
        intervalWayEntity.setTariffs(tariffs);
        intervalWayRepository.save(intervalWayEntity);
        List<IntervalWayEntity> intervalWayEntityList = wayEntity.getIntervalWays();
        for(int i = 0; i < intervalWayEntityList.size(); i++){
            if (intervalWayEntityList.get(i).getId() == intervalWayId) intervalWayEntityList.remove(i);
        }
        intervalWayEntityList.add(intervalWayEntity);
        wayEntity.setIntervalWays(intervalWayEntityList);
        wayRepository.save(wayEntity);
    }

    public void deleteIntervalWay(Long companyId, Long wayId, Long intervalWayId){
        CompanyEntity companyEntity = companyRepository.findCompanyEntityById(companyId);
        WayEntity wayEntity = wayRepository.findWayEntityById(wayId);
        IntervalWayEntity intervalWayEntity = intervalWayRepository.findIntervalWayEntityById(intervalWayId);
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        for(OrderEntity order : orderEntityList){
            if(order.getIntervalWay().getId() == intervalWayId) throw new BadRequestException("Этот путь присутствует в заказах, его нельзя удалить");
        }
        if ((wayEntity == null)||(companyEntity == null)||(intervalWayEntity == null)) throw new NotFoundException("Данные для удаления не найдены");
        List<IntervalWayEntity> intervalWayEntityList = wayEntity.getIntervalWays();
        for(int i=0; i < intervalWayEntityList.size(); i++){
            if(intervalWayEntityList.get(i).getId() == intervalWayId){
                intervalWayEntityList.remove(i);
            }
        }
        wayEntity.setIntervalWays(intervalWayEntityList);

        List<WayEntity> wayEntityList = companyEntity.getWays();
        for(int i=0; i < wayEntityList.size(); i++){
            if(wayEntityList.get(i).getId() == wayEntity.getId()){
                wayEntityList.get(i).setId(wayEntity.getId());
                wayEntityList.get(i).setStartPoint(wayEntity.getStartPoint());
                wayEntityList.get(i).setEndPoint(wayEntity.getEndPoint());
                wayEntityList.get(i).setPointNumber(wayEntity.getPointNumber());
                wayEntityList.get(i).setIntervalWays(wayEntity.getIntervalWays());
            }
        }
        companyEntity.setWays(wayEntityList);
        wayRepository.save(wayEntity);
        companyRepository.save(companyEntity);
        intervalWayRepository.deleteById(intervalWayEntity.getId());
    }
}

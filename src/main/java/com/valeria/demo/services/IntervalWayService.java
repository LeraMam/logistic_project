package com.valeria.demo.services;

import com.valeria.demo.db.entity.IntervalWayEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.entity.WayEntity;
import com.valeria.demo.db.repositories.CompanyRepository;
import com.valeria.demo.db.repositories.IntervalWayRepository;
import com.valeria.demo.db.repositories.TariffRepository;
import com.valeria.demo.db.repositories.WayRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntervalWayService {
    private final IntervalWayRepository intervalWayRepository;
    private final WayRepository wayRepository;
    private final TariffRepository tariffRepository;

    public IntervalWayService(IntervalWayRepository intervalWayRepository, WayRepository wayRepository, TariffRepository tariffRepository) {
        this.intervalWayRepository = intervalWayRepository;
        this.wayRepository = wayRepository;
        this.tariffRepository = tariffRepository;
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

}

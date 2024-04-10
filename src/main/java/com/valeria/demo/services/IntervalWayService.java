package com.valeria.demo.services;

import com.valeria.demo.db.entity.IntervalWayEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.entity.WayEntity;
import com.valeria.demo.db.repositories.IntervalWayRepository;
import com.valeria.demo.db.repositories.TariffRepository;
import com.valeria.demo.db.repositories.WayRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        intervalWayRepository.save(intervalWayEntity);

        List<IntervalWayEntity> intervalWayEntityList = wayEntity.getIntervalWays();
        intervalWayEntityList.add(intervalWayEntity);

        wayEntity.setIntervalWays(intervalWayEntityList);
        wayRepository.save(wayEntity);
    }

}

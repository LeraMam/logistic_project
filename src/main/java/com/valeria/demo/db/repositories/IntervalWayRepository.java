package com.valeria.demo.db.repositories;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.IntervalWayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntervalWayRepository extends JpaRepository<IntervalWayEntity, Long> {
    IntervalWayEntity findIntervalWayEntityById(Long id);
}

package com.valeria.demo.db.repositories;

import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.entity.WayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WayRepository extends JpaRepository<WayEntity, Long> {
    WayEntity findWayEntityById(Long id);
}

package com.valeria.demo.db.repositories;

import com.valeria.demo.db.entity.TariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<TariffEntity, Long> {
}

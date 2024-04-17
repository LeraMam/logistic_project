package com.valeria.demo.db.repositories;

import com.valeria.demo.db.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    ItemEntity findItemEntityById(Long id);
}

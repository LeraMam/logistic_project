package com.valeria.demo.db.repositories;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.OrderEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findOrderEntitiesByCompany(CompanyEntity company);
    List<OrderEntity> findOrderEntitiesByUser(UserEntity userEntity);
}

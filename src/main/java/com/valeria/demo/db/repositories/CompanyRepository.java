package com.valeria.demo.db.repositories;

import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository  extends JpaRepository<CompanyEntity, Long> {

}

package com.valeria.demo.services;

import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.repositories.TariffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TariffService {
    private final TariffRepository tariffRepository;
    @Autowired
    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }
    public TariffEntity addNewTariff(TariffEntity tariffEntity){
        return tariffRepository.save(tariffEntity);
    }
}

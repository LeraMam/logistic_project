package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.services.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/about")
public class TariffController {
    private final TariffService tariffService;
    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @GetMapping("/tariff/{companyId}")
    public List<TariffEntity> getAllTariffsForCompany(@PathVariable Long companyId){
        return tariffService.getAllTariffsForCompany(companyId);
    }

    @PostMapping("/tariff/{companyId}")
    public void addTariff(@PathVariable Long companyId, @RequestBody TariffEntity tariffEntity){
        tariffService.addNewTariff(companyId, tariffEntity);
    }

    @PutMapping("/tariff/{companyId}/{tariffId}")
    public TariffEntity editTariff(@PathVariable Long companyId,@PathVariable Long tariffId, @RequestBody TariffEntity tariffEntity){
        System.out.println(tariffEntity);
        System.out.println(companyId);
        System.out.println(tariffId);
        System.out.println("Put ");
        return tariffService.editTariff(companyId, tariffId, tariffEntity);
    }

    @DeleteMapping("/{tariffId}")
    public void deleteTariff(@PathVariable Long tariffId){
        tariffService.deleteTariff(tariffId);
    }
}

package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.services.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/about")
public class TariffController {
    private final TariffService tariffService;
    @Autowired
    public TariffController(TariffService tariffService) {
        this.tariffService = tariffService;
    }

    @PostMapping("/tariff/{companyId}")
    public void addTariff(@PathVariable Long companyId, @RequestBody TariffEntity tariffEntity){
        System.out.println(tariffEntity);
        tariffService.addNewTariff(companyId, tariffEntity);
    }
}

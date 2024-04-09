package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.services.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/tariffs")
    public List<TariffEntity> getAllTariffs(
            /*@RequestParam(value = "deliveryType", required = false) List<String> deliveryType,
            @RequestParam(value = "startPoint", required = false) String startPoint,
            @RequestParam(value = "endPoint", required = false) String endPoint,
            @RequestParam(value = "price", required = false) Double maxPrice,
            @RequestParam(value = "maxWeight", required = false) Double maxWeight,
            @RequestParam(value = "distance", required = false) Double maxDistance,
            @RequestParam(value = "time", required = false) Double maxTime,*/
            @RequestBody(required = false) TariffEntity searchTariffEntity){
        System.out.println("Работает контроллер...");

        List<TariffEntity> filterList =  tariffService.getAllTariffs()
                .stream()
                .filter(tariffEntity -> searchTariffEntity.getType() == null || tariffEntity.getType() == searchTariffEntity.getType())
                .filter(tariffEntity -> searchTariffEntity.getStartPoint() == null || tariffEntity.getStartPoint().contains(searchTariffEntity.getStartPoint()))
                .filter(tariffEntity -> searchTariffEntity.getEndPoint() == null || tariffEntity.getEndPoint().contains(searchTariffEntity.getEndPoint()))
                .filter(tariffEntity -> searchTariffEntity.getPrice() == null || tariffEntity.getPrice() * tariffEntity.getDistance() <= searchTariffEntity.getPrice())//общие расходы
                .filter(tariffEntity -> searchTariffEntity.getMaxWeight() == null || tariffEntity.getMaxWeight() <= searchTariffEntity.getMaxWeight())
                .filter(tariffEntity -> searchTariffEntity.getDistance() == null || tariffEntity.getDistance() <= searchTariffEntity.getDistance())
                .filter(tariffEntity -> searchTariffEntity.getTime() == null || tariffEntity.getTime() <= searchTariffEntity.getTime())
                .collect(Collectors.toList());
        return  filterList;
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

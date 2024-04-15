package com.valeria.demo.controllers;

import com.valeria.demo.additional.SearchClass;
import com.valeria.demo.db.entity.CompanyEntity;
import com.valeria.demo.db.entity.IntervalWayEntity;
import com.valeria.demo.db.entity.TariffEntity;
import com.valeria.demo.db.entity.WayEntity;
import com.valeria.demo.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/about")
public class CompanyController {
    private final CompanyService companyService;
    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/one/{companyId}")
    public CompanyEntity getOneCompany(@PathVariable Long companyId){
        return companyService.findCompanyById(companyId);
    }

    @PostMapping("/company/all")
    public List<CompanyEntity> getAllCompanies(@RequestBody(required = false) SearchClass searchParams) {
        List<CompanyEntity> filteredCompanies = new ArrayList<>();
        List<CompanyEntity> allCompanies = companyService.getAllCompanies();
        for (CompanyEntity company : allCompanies) {
            List<WayEntity> filteredWays = new ArrayList<>();
            for (WayEntity wayEntity : company.getWays()) {
                List<IntervalWayEntity> filteredIntervalWays = new ArrayList<>();
                for (IntervalWayEntity intervalWayEntity : wayEntity.getIntervalWays()) {
                    List<TariffEntity> filteredTariffs = new ArrayList<>();
                    for(TariffEntity tariffEntity : intervalWayEntity.getTariffs()){
                        boolean isTariffMatched = isTariffMatched(tariffEntity, searchParams);
                        if (isTariffMatched) {
                            filteredTariffs.add(tariffEntity);
                        }
                    }
                    boolean isIntervalMatched = isIntervalWayMatched(intervalWayEntity, searchParams);
                    if ((isIntervalMatched) && (!filteredTariffs.isEmpty())) {
                        filteredIntervalWays.add(intervalWayEntity);
                    }
                }
                if(searchParams.isSortByTime()) filteredIntervalWays.sort(Comparator.comparing(IntervalWayEntity::getSumTime));
                if(searchParams.isSortByPrice()) filteredIntervalWays.sort(Comparator.comparing(IntervalWayEntity::getSumPrice));

                boolean isWayMatched = isWayMatched(wayEntity, searchParams);
                if ((isWayMatched) && (!filteredIntervalWays.isEmpty())) {
                    WayEntity filteredWay = new WayEntity();
                    filteredWay.setId(wayEntity.getId());
                    filteredWay.setStartPoint(wayEntity.getStartPoint());
                    filteredWay.setEndPoint(wayEntity.getEndPoint());
                    filteredWay.setPointNumber(wayEntity.getPointNumber());
                    filteredWay.setIntervalWays(filteredIntervalWays);
                    filteredWays.add(filteredWay);
                }
            }
            if(searchParams.isSortByTime()) filteredWays.sort(Comparator.comparing(wayEntity -> wayEntity.getIntervalWays().get(0).getSumTime()));
            if(searchParams.isSortByPrice()) filteredWays.sort(Comparator.comparing(wayEntity -> wayEntity.getIntervalWays().get(0).getSumPrice()));

            if (!filteredWays.isEmpty()) {
                CompanyEntity filteredCompany = new CompanyEntity();
                filteredCompany.setId(company.getId());
                filteredCompany.setName(company.getName());
                filteredCompany.setEmail(company.getEmail());
                filteredCompany.setDescription(company.getDescription());
                filteredCompany.setWays(filteredWays);
                filteredCompany.setTariffs(company.getTariffs());
                filteredCompanies.add(filteredCompany);
            }
        }
        if(searchParams.isSortByTime()) filteredCompanies.sort(Comparator.comparing(companyEntity -> companyEntity.getWays().get(0).getIntervalWays().get(0).getSumTime()));
        if(searchParams.isSortByPrice()) filteredCompanies.sort(Comparator.comparing(companyEntity -> companyEntity.getWays().get(0).getIntervalWays().get(0).getSumPrice()));
        return filteredCompanies;
    }

    @PostMapping("/company")
    public void addCompany(@RequestBody CompanyEntity companyEntity){
        companyService.addNewCompany(companyEntity);
    }

    private boolean isWayMatched(WayEntity way, SearchClass searchParams) {
        boolean isStartPointMatched = searchParams.getStartPoint() == null || way.getStartPoint().contains(searchParams.getStartPoint());
        boolean isEndPointMatched = searchParams.getEndPoint() == null || way.getEndPoint().contains(searchParams.getEndPoint());
        return isStartPointMatched && isEndPointMatched;
    }
    private boolean isIntervalWayMatched(IntervalWayEntity intervalWay, SearchClass searchParams) {
        boolean isWeightMatched = searchParams.getMaxWeight() == null || intervalWay.getMaxWeight() >= searchParams.getMaxWeight();
        boolean isPriceMatched = searchParams.getSumPrice() == null || intervalWay.getSumPrice() <= searchParams.getSumPrice();
        boolean isTimeMatched = searchParams.getSumTime() == null || intervalWay.getSumTime() <= searchParams.getSumTime();
        return isWeightMatched && isPriceMatched && isTimeMatched;
    }

    private boolean isTariffMatched(TariffEntity tariff, SearchClass searchParams) {
        boolean isStartPointMatched = searchParams.getIntervalPoint() == null || tariff.getStartPoint().contains(searchParams.getIntervalPoint());
        boolean isEndPointMatched = searchParams.getIntervalPoint() == null || tariff.getEndPoint().contains(searchParams.getIntervalPoint());
        boolean isTypeMatched = searchParams.getType() == null || tariff.getType() == (searchParams.getType());
        return (isStartPointMatched || isEndPointMatched) && isTypeMatched;
    }
}

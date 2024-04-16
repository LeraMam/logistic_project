package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.WayEntity;
import com.valeria.demo.services.IntervalWayService;
import com.valeria.demo.services.WayService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/way")
public class WayController {
    private final WayService wayService;
    private final IntervalWayService intervalWayService;

    public WayController(WayService wayService, IntervalWayService intervalWayService) {
        this.wayService = wayService;
        this.intervalWayService = intervalWayService;
    }

    @GetMapping("/{companyId}")
    public List<WayEntity> getAllWaysForCompany(@PathVariable Long companyId){
        return wayService.getAllWaysForCompany(companyId);
    }
    @PostMapping("/add/{companyId}")
    public void addWay(@PathVariable Long companyId, @RequestBody WayEntity wayEntity){
        wayService.addNewWay(companyId, wayEntity);
    }

    @PutMapping("/edit/{companyId}/{wayId}")
    public void editWay(@PathVariable Long companyId, @PathVariable Long wayId, @RequestBody WayEntity wayEntity){
        wayService.editWay(companyId, wayId, wayEntity);
    }

    @DeleteMapping("/delete/{companyId}/{wayId}")
    public void deleteWay(@PathVariable Long companyId, @PathVariable Long wayId){
        wayService.deleteWay(companyId, wayId);
    }

    @PostMapping("/add/interval/{wayId}")
    public void addIntervalWay(@PathVariable Long wayId, @RequestBody ArrayList<Long> dataList){
        intervalWayService.addNewIntervalWay(wayId, dataList);
    }

    @PutMapping("/edit/interval/{wayId}/{intervalWayId}")
    public void editIntervalWay(@PathVariable Long wayId, @PathVariable Long intervalWayId, @RequestBody ArrayList<Long> dataList){
        intervalWayService.editIntervalWay(wayId, intervalWayId, dataList);
    }

    @DeleteMapping("/delete/interval/{companyId}/{wayId}/{intervalWayId}")
    public void deleteIntervalWay(@PathVariable Long companyId, @PathVariable Long wayId, @PathVariable Long intervalWayId){
        intervalWayService.deleteIntervalWay(companyId, wayId, intervalWayId);
    }
}

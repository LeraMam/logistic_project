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

    @PostMapping("/add/interval/{wayId}")
    public void addIntervalWay(@PathVariable Long wayId, @RequestBody ArrayList<Long> dataList){
        System.out.println(wayId);
        System.out.println(dataList);
        intervalWayService.addNewIntervalWay(wayId, dataList);
    }
}

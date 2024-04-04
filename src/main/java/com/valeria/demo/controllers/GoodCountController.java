package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.GoodsEntity;
import com.valeria.demo.services.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/count")
public class GoodCountController {
    private final GoodsService goodsService;
    @Autowired
    public GoodCountController(GoodsService goodsService) {
        this.goodsService = goodsService;
    }

    @PostMapping
    public void addCountDate(@RequestBody GoodsEntity goodsEntity){
        /*System.out.println(goodsEntity);*/
        goodsService.addNewGoods(goodsEntity);
    }
}

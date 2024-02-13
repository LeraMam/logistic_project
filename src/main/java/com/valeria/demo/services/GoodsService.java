package com.valeria.demo.services;

import com.valeria.demo.db.entity.GoodsEntity;
import com.valeria.demo.db.repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsService {
    private final GoodsRepository goodsRepository;
    @Autowired
    public GoodsService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    public GoodsEntity addNewGoods(GoodsEntity newGoodsEntity){
        return goodsRepository.save(newGoodsEntity);
    }
}

package com.valeria.demo.services;

import com.valeria.demo.db.entity.ItemEntity;
import com.valeria.demo.db.repositories.ItemRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemEntity addNewItem(ItemEntity itemEntity){
        return itemRepository.save(itemEntity);
    }
}

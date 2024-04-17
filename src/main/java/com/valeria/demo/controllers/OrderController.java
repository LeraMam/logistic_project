package com.valeria.demo.controllers;

import com.valeria.demo.additional.BackpackResult;
import com.valeria.demo.db.entity.ItemEntity;
import com.valeria.demo.db.entity.OrderEntity;
import com.valeria.demo.services.ItemService;
import com.valeria.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final ItemService itemService;

    @Autowired
    public OrderController(OrderService orderService, ItemService itemService) {
        this.orderService = orderService;
        this.itemService = itemService;
    }
    @GetMapping("/companies")
    public List<OrderEntity> getOrdersForCompany(){
        orderService.calculateBackpackWeightForOrder();
        return orderService.findOrdersForCompany();
    }

    @GetMapping("/optimisation")
    public BackpackResult getOptimisationWeightForOrder(){
        return orderService.calculateBackpackWeightForOrder();
    }

    @GetMapping("/users")
    public List<OrderEntity> getOrdersForUser(){
        return orderService.findOrdersForUser();
    }
    @PostMapping("/add/{itemId}")
    public void addOrder(@RequestBody OrderEntity orderEntity, @PathVariable Long itemId){
        orderService.addOrder(orderEntity, itemId);
    }

    @PostMapping("/add/item")
    public ItemEntity addItem(@RequestBody ItemEntity itemEntity){
        return itemService.addNewItem(itemEntity);
    }

    @PutMapping("/state")
    public void changeOrderState(@RequestBody OrderEntity orderEntity){
        orderService.changeOrder(orderEntity);
    }
}

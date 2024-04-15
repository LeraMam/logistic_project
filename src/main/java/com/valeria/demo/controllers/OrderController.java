package com.valeria.demo.controllers;

import com.valeria.demo.db.entity.OrderEntity;
import com.valeria.demo.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @GetMapping("/companies")
    public List<OrderEntity> getOrdersForCompany(){
        return orderService.findOrdersForCompany();
    }
    @GetMapping("/users")
    public List<OrderEntity> getOrdersForUser(){
        return orderService.findOrdersForUser();
    }
    @PostMapping("/add")
    public void addOrder(@RequestBody OrderEntity orderEntity){
        orderService.addOrder(orderEntity);
    }

    @PutMapping("/state")
    public void changeOrderState(@RequestBody OrderEntity orderEntity){
        orderService.changeOrder(orderEntity);
    }
}

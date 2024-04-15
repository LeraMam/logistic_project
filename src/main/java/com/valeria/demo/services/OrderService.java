package com.valeria.demo.services;

import com.valeria.demo.db.entity.*;
import com.valeria.demo.db.repositories.*;
import com.valeria.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final WayRepository wayRepository;
    private final IntervalWayRepository intervalWayRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CompanyRepository companyRepository, WayRepository wayRepository, IntervalWayRepository intervalWayRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.wayRepository = wayRepository;
        this.intervalWayRepository = intervalWayRepository;
    }

    public List<OrderEntity> findOrdersForCompany(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        Optional<UserEntity> user = userRepository.findUserEntityByLogin(username);
        UserEntity findUserEntity = new UserEntity();
        if (user.isPresent()) {
            findUserEntity = user.get();
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        CompanyEntity companyEntity = findUserEntity.getCompany();
        return orderRepository.findOrderEntitiesByCompany(companyEntity);
    }

    public List<OrderEntity> findOrdersForUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        Optional<UserEntity> user = userRepository.findUserEntityByLogin(username);
        UserEntity findUserEntity = new UserEntity();
        if (user.isPresent()) {
            findUserEntity = user.get();
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        List<OrderEntity> findOrders = orderRepository.findOrderEntitiesByUser(findUserEntity);
        return findOrders;
    }

    public void addOrder(OrderEntity orderEntity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            username = userDetails.getUsername();
        }
        Optional<UserEntity> user = userRepository.findUserEntityByLogin(username);
        UserEntity findUserEntity = new UserEntity();
        if (user.isPresent()) {
            findUserEntity = user.get();
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
        /*CompanyEntity companyEntity = orderEntity.getCompany();
        WayEntity wayEntity = orderEntity.getWay();
        IntervalWayEntity intervalWayEntity = orderEntity.getIntervalWay();*/

        CompanyEntity managedCompanyEntity = companyRepository.findById(orderEntity.getCompany().getId())
                .orElseThrow(() -> new NotFoundException("Компания не найдена"));

        WayEntity managedWayEntity = wayRepository.findById(orderEntity.getWay().getId())
                .orElseThrow(() -> new NotFoundException("Путь не найден"));

        IntervalWayEntity managedIntervalWayEntity = intervalWayRepository.findById(orderEntity.getIntervalWay().getId())
                .orElseThrow(() -> new NotFoundException("Промежуточный путь не найден"));

        OrderEntity newOrder = new OrderEntity();
        newOrder.setState(OrderState.WAITING);
        newOrder.setCompany(managedCompanyEntity);
        newOrder.setWay(managedWayEntity);
        newOrder.setIntervalWay(managedIntervalWayEntity);
        newOrder.setUser(findUserEntity);
        OrderEntity savedOrder = orderRepository.save(newOrder);
    }

    public void changeOrder(OrderEntity orderEntity){
        System.out.println(orderEntity);
        OrderEntity changedOrder = new OrderEntity();
        changedOrder.setId(orderEntity.getId());
        changedOrder.setState(orderEntity.getState());
        changedOrder.setUser(orderEntity.getUser());
        changedOrder.setCompany(orderEntity.getCompany());
        changedOrder.setWay(orderEntity.getWay());
        changedOrder.setIntervalWay(orderEntity.getIntervalWay());
        if(orderEntity.getState() == OrderState.WAITING){
            changedOrder.setState(OrderState.IN_PROCESSING);
        }
        else if(orderEntity.getState() == OrderState.IN_PROCESSING){
            changedOrder.setState(OrderState.RESOLVED);
        }
        System.out.println(changedOrder);
        OrderEntity savedOrder = orderRepository.save(changedOrder);
    }
}

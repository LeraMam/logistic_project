package com.valeria.demo.services;

import com.valeria.demo.additional.Nabor;
import com.valeria.demo.db.entity.*;
import com.valeria.demo.db.repositories.*;
import com.valeria.demo.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final WayRepository wayRepository;
    private final IntervalWayRepository intervalWayRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, CompanyRepository companyRepository, WayRepository wayRepository, IntervalWayRepository intervalWayRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.wayRepository = wayRepository;
        this.intervalWayRepository = intervalWayRepository;
        this.itemRepository = itemRepository;
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

    public void addOrder(OrderEntity orderEntity, Long itemId){
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
        ItemEntity item = itemRepository.findItemEntityById(itemId);
        newOrder.setItem(item);
        OrderEntity savedOrder = orderRepository.save(newOrder);
    }

    public void changeOrder(OrderEntity orderEntity){
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
        OrderEntity savedOrder = orderRepository.save(changedOrder);
    }

    public Nabor calculateBackpackWeightForOrder(){
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
        List<OrderEntity> orderEntityList = orderRepository.findAll();
        double maxTC = companyEntity.getMaxTC();
        long maxWeightLong = (long) maxTC;
        BigInteger maxWeight = BigInteger.valueOf(maxWeightLong);
        Nabor nabor = backpack(orderEntityList, maxWeight);
        return nabor;
    }

    public Nabor backpack(List<OrderEntity> orders, BigInteger maxWeight) {
        Set<Long> seen = new HashSet<>();
        Nabor result = new Nabor(new ArrayList<>(), BigInteger.ZERO, BigInteger.ZERO);
        bruteforce(orders, maxWeight, seen, result, BigInteger.ZERO, BigInteger.ZERO);
        return result;
    }

    private void bruteforce(List<OrderEntity> orders, BigInteger maxWeight, Set<Long> seen, Nabor result,
                            BigInteger sumPrice, BigInteger sumWeight) {
        boolean isLeaf = true;
        for (OrderEntity order : orders) {
            if (seen.contains(order.getId())) {
                continue;
            }
            ItemEntity item = order.getItem();
            if (item.getWeight().add(sumWeight).compareTo(maxWeight) > 0) {
                continue;
            }

            seen.add(order.getId());
            bruteforce(orders, maxWeight, seen, result, sumPrice.add(item.getPrice()), sumWeight.add(item.getWeight()));
            seen.remove(order.getId());
            isLeaf = false;
        }

        if (isLeaf && sumPrice.compareTo(result.getPrice()) >= 0) {
            result.setPrice(sumPrice);
            result.setWeight(sumWeight);
            result.setResults(new ArrayList<>(seen));
        }
    }
}

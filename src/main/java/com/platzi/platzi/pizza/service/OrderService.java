package com.platzi.platzi.pizza.service;

import com.platzi.platzi.pizza.persistence.entity.OrderEntity;
import com.platzi.platzi.pizza.persistence.projection.OrderSummary;
import com.platzi.platzi.pizza.persistence.repository.OrderRepository;
import com.platzi.platzi.pizza.service.dto.RandomOrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private static final String DELIVERY = "D";
    private static final String CARRYOUT = "C";
    private static final String ON_SITE = "S";


    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderEntity> getAllOrders(){
        List<OrderEntity> orders = this.orderRepository.findAll();
        orders.forEach(o -> System.out.println(o.getCustomer().getName()));
        return orders;
    }

    public List<OrderEntity> getTodayOrders(){
        LocalDateTime today = LocalDate.now().atTime(0, 0);
        return this.orderRepository.findAllByDateAfter(today);
    }

    public List<OrderEntity> getOutsideOrders(){
        List<String> methods = Arrays.asList(DELIVERY, CARRYOUT);
        return this.orderRepository.findAllByMethodIn(methods);
    }

    @Secured("ROLE_ADMIN")
    public List<OrderEntity> getCustomerOrders(String idCustomer){
        return this.orderRepository.findCustomersOrders(idCustomer);
    }

    public OrderSummary getSummary(int idOrder){
        return this.orderRepository.findSummary(idOrder);
    }

    /**
     * This always return true because I didn't create a query to execute on the SQL database
     */
    @Transactional
    public boolean saveRandomOrder(RandomOrderDto randomOrderDto){

        return true;
    }
}

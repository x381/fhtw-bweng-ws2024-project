package at.fhtw.bweng_ws24.service;


import at.fhtw.bweng_ws24.repository.OrderRepository;
import org.springframework.stereotype.Service;
import at.fhtw.bweng_ws24.model.Order;

import java.util.List;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    //private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        //this.orderMapper = orderMapper;
    }

    public List<Order> getOrders() {
        return orderRepository.findAllOrders();
    }

    /*public Order getOrder(UUID id){
        return orderRepository.findById(id);
    }*/
}

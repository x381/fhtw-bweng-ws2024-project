package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.mapper.OrderMapper;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.repository.OrderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import at.fhtw.bweng_ws24.model.Order;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, PasswordEncoder passwordEncoder) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<Order> getOrders() {
        return orderRepository.findAllOrders();
    }

    /*public Order getOrder(UUID id){
        return orderRepository.findById(id);
    }*/
}

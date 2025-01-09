package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.OrderDto;
import at.fhtw.bweng_ws24.mapper.OrderMapper;
import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.model.OrderItem;
import at.fhtw.bweng_ws24.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll(); // Assumes `OrderRepository` extends `JpaRepository`
    }

    public Order getOrder(UUID id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Order not found for id: " + id));
    }

    public UUID createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerEmail(orderDto.getCustomerEmail());
        order.setAddress(orderDto.getAddress());
        order.setTotalAmount((float) orderDto.getTotalAmount()); // Explicit conversion to match `Float`

        // Convert List<String> (order items) to List<OrderItem>
        List<OrderItem> orderItems = orderDto.getOrderItems().stream()
                .map(productId -> new OrderItem(null, productId, 1)) // Default quantity is 1
                .collect(Collectors.toList());
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return savedOrder.getId();
    }

    public void updateOrder(UUID id, OrderDto orderDto) {
        Order existingOrder = orderRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Order not found for id: " + id));

        existingOrder.setCustomerName(orderDto.getCustomerName());
        existingOrder.setCustomerEmail(orderDto.getCustomerEmail());
        existingOrder.setAddress(orderDto.getAddress());
        existingOrder.setTotalAmount((float) orderDto.getTotalAmount()); // Explicit conversion to match `Float`

        // Convert List<String> (order items) to List<OrderItem>
        List<OrderItem> orderItems = orderDto.getOrderItems().stream()
                .map(productId -> new OrderItem(null, productId, 1)) // Default quantity is 1
                .collect(Collectors.toList());
        existingOrder.setOrderItems(orderItems);

        orderRepository.save(existingOrder);
    }

    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found for id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
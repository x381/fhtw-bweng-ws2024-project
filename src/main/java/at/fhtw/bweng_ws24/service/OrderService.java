package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.OrderDto;
import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.model.OrderItem;
import at.fhtw.bweng_ws24.model.OrderStatus;
import at.fhtw.bweng_ws24.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository orderRepository;

    public OrderService(ProductService productService, OrderRepository orderRepository) {
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    public Order getOrder(UUID id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Order not found for id: " + id));
    }

    public List<Order> getOrdersByUserId(UUID createdBy) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getCreatedBy().equals(createdBy))
                .collect(Collectors.toList());
    }

    @Transactional
    public UUID createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setCustomerName(orderDto.getCustomerName());
        order.setCustomerEmail(orderDto.getCustomerEmail());
        order.setAddress(orderDto.getAddress());
        order.setTotalAmount((float) orderDto.getTotalPrice());
        order.setCreatedBy(UUID.fromString(orderDto.getCreatedBy()));

        List<OrderItem> orderItems = orderDto.getOrderItems().stream()
                .map(orderItemDto -> new OrderItem(null, orderItemDto.getProductId(), orderItemDto.getProductName(), orderItemDto.getQuantity())).toList();
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        orderItems.forEach(orderItem -> {
            productService.updateStock(UUID.fromString(orderItem.getProductId()), orderItem.getQuantity());
        });

        return savedOrder.getId();
    }

    public void deleteOrder(UUID id) {
        if (!orderRepository.existsById(id)) {
            throw new IllegalArgumentException("Order not found for id: " + id);
        }
        orderRepository.deleteById(id);
    }

    public void confirmOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("Order not found for id: " + orderId));
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }
}
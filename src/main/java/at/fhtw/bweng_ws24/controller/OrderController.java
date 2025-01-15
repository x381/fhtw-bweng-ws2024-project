package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.OrderDto;
import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable UUID id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/user/{createdBy}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and hasPermission(#createdBy, 'at.fhtw.bweng_ws24.model.User', 'read'))")
    public List<Order> getOrdersByCreatedBy(@PathVariable UUID createdBy) {
        return orderService.getOrdersByUserId(createdBy);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDto orderDto) {
        UUID orderId = orderService.createOrder(orderDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order created successfully");
        response.put("orderId", orderId);
        return ResponseEntity
                .created(URI.create("/orders/" + orderId))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasPermission(#id, 'at.fhtw.bweng_ws24.model.Order', 'delete')")
    public ResponseEntity<Order> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
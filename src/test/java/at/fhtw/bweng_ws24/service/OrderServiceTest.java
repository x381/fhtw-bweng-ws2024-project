package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.OrderDto;
import at.fhtw.bweng_ws24.dto.OrderItemDto;
import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.model.OrderItem;
import at.fhtw.bweng_ws24.model.OrderStatus;
import at.fhtw.bweng_ws24.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private ProductService productService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrders_shouldReturnAllOrders() {
        // Arrange
        List<Order> expectedOrders = Arrays.asList(
                new Order(UUID.randomUUID(), "John Doe", "john.doe@example.com", "123 Street, City", 100.0f, UUID.randomUUID(), new ArrayList<>(), OrderStatus.PENDING, null),
                new Order(UUID.randomUUID(), "Jane Smith", "jane.smith@example.com", "456 Avenue, City", 200.0f, UUID.randomUUID(), new ArrayList<>(), OrderStatus.PENDING, null)
        );

        when(orderRepository.findAll()).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.getOrders();

        // Assert
        assertEquals(expectedOrders.size(), actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void getOrder_shouldReturnOrderById() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order expectedOrder = new Order(orderId, "John Doe", "john.doe@example.com", "123 Street, City", 100.0f, UUID.randomUUID(), new ArrayList<>(), OrderStatus.PENDING, null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));

        // Act
        Order actualOrder = orderService.getOrder(orderId);

        // Assert
        assertNotNull(actualOrder);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void getOrder_shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.getOrder(orderId));
        assertEquals("Order not found for id: " + orderId, exception.getMessage());
    }

    @Test
    void createOrder_shouldSaveOrderAndUpdateStock() {
        // Arrange
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId("product-id");
        orderItemDto.setProductName("Product Name");
        orderItemDto.setQuantity(2);
        List<OrderItemDto> orderItemsDto = List.of(orderItemDto);

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerName("John Doe");
        orderDto.setCustomerEmail("john.doe@example.com");
        orderDto.setAddress("123 Street, City");
        orderDto.setTotalPrice(100.0);
        orderDto.setCreatedBy(UUID.randomUUID().toString());
        orderDto.setOrderItems(orderItemsDto);

        Order savedOrder = new Order(UUID.randomUUID(), "John Doe", "john.doe@example.com", "123 Street, City", 100.0f, UUID.fromString(orderDto.getCreatedBy()), new ArrayList<>(), OrderStatus.PENDING, null);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // Act
        UUID createdOrderId = orderService.createOrder(orderDto);

        // Assert
        assertNotNull(createdOrderId);
        assertEquals(savedOrder.getId(), createdOrderId);
        verify(productService, times(1)).updateStock(UUID.fromString(orderItemDto.getProductId()), orderItemDto.getQuantity());
    }

    @Test
    void deleteOrder_shouldDeleteOrderWhenExists() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.existsById(orderId)).thenReturn(true);

        // Act
        orderService.deleteOrder(orderId);

        // Assert
        verify(orderRepository, times(1)).deleteById(orderId);
    }

    @Test
    void deleteOrder_shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.existsById(orderId)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.deleteOrder(orderId));
        assertEquals("Order not found for id: " + orderId, exception.getMessage());
    }

    @Test
    void confirmOrder_shouldUpdateOrderStatusToConfirmed() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = new Order(orderId, "John Doe", "john.doe@example.com", "123 Street, City", 100.0f, UUID.randomUUID(), new ArrayList<>(), OrderStatus.PENDING, null);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        orderService.confirmOrder(orderId);

        // Assert
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void confirmOrder_shouldThrowExceptionWhenOrderNotFound() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> orderService.confirmOrder(orderId));
        assertEquals("Order not found for id: " + orderId, exception.getMessage());
    }
}
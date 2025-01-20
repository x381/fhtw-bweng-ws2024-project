package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.OrderDto;
import at.fhtw.bweng_ws24.dto.OrderItemDto;
import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.model.OrderStatus;
import at.fhtw.bweng_ws24.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetOrders() throws Exception {
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        List<Order> orders = Arrays.asList(
                new Order(orderId1, "John Doe", "john@example.com", "123 Main St", 100.0f, UUID.randomUUID(), null, OrderStatus.PENDING, Instant.now()),
                new Order(orderId2, "Jane Doe", "jane@example.com", "456 Elm St", 200.0f, UUID.randomUUID(), null, OrderStatus.CONFIRMED, Instant.now())
        );
        when(orderService.getOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(orderId1.toString()))
                .andExpect(jsonPath("$[0].customerName").value("John Doe"))
                .andExpect(jsonPath("$[0].status").value("PENDING"))
                .andExpect(jsonPath("$[1].id").value(orderId2.toString()))
                .andExpect(jsonPath("$[1].customerName").value("Jane Doe"))
                .andExpect(jsonPath("$[1].status").value("CONFIRMED"));
    }

    @Test
    void testGetOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID createdBy = UUID.randomUUID();
        Order order = new Order(orderId, "John Doe", "john@example.com", "123 Main St", 100.0f, createdBy, null, OrderStatus.PENDING, Instant.now());
        when(orderService.getOrder(orderId)).thenReturn(order);

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderId.toString()))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.customerEmail").value("john@example.com"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.totalAmount").value(100.0))
                .andExpect(jsonPath("$.createdBy").value(createdBy.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testGetOrdersByCreatedBy() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID orderId1 = UUID.randomUUID();
        UUID orderId2 = UUID.randomUUID();
        List<Order> orders = Arrays.asList(
                new Order(orderId1, "John Doe", "john@example.com", "123 Main St", 100.0f, userId, null, OrderStatus.PENDING, Instant.now()),
                new Order(orderId2, "John Doe", "john@example.com", "456 Elm St", 200.0f, userId, null, OrderStatus.CONFIRMED, Instant.now())
        );
        when(orderService.getOrdersByUserId(userId)).thenReturn(orders);

        mockMvc.perform(get("/orders/user/{createdBy}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(orderId1.toString()))
                .andExpect(jsonPath("$[0].createdBy").value(userId.toString()))
                .andExpect(jsonPath("$[1].id").value(orderId2.toString()))
                .andExpect(jsonPath("$[1].createdBy").value(userId.toString()));
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setProductId(String.valueOf(UUID.randomUUID()));
        orderItemDto.setProductName("Product 1");
        orderItemDto.setQuantity(2);

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerName("John Doe");
        orderDto.setCustomerEmail("john@example.com");
        orderDto.setAddress("123 Main St");
        orderDto.setTotalPrice(100.0f);
        orderDto.setCreatedBy(UUID.randomUUID().toString());
        orderDto.setOrderItems(List.of(orderItemDto));

        UUID orderId = UUID.randomUUID();
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(orderId);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/orders/" + orderId))
                .andExpect(jsonPath("$.message").value("Order created successfully"))
                .andExpect(jsonPath("$.orderId").value(orderId.toString()));
    }

    @Test
    void testDeleteOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        doNothing().when(orderService).deleteOrder(orderId);

        mockMvc.perform(delete("/orders/{id}", orderId))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(orderId);
    }

    @Test
    void testConfirmOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        doNothing().when(orderService).confirmOrder(orderId);

        mockMvc.perform(post("/orders/{orderId}/confirm", orderId))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).confirmOrder(orderId);
    }
}

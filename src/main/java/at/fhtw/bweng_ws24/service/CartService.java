package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.CartDto;
import at.fhtw.bweng_ws24.model.*;
import at.fhtw.bweng_ws24.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getCarts() {
        return cartRepository.findAll();
    }

    public Cart getCart(UUID id) {
        return cartRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Cart not found for id: " + id));
    }

    public List<Cart> getCartsByUserId(UUID createdBy) {
        return cartRepository.findAll().stream()
                .filter(cart -> cart.getCreatedBy().equals(createdBy))
                .collect(Collectors.toList());
    }

    @Transactional
    public UUID createCart(CartDto cartDto) {
        Cart cart = new Cart();
        cart.setCreatedBy(UUID.fromString(cartDto.getCreatedBy()));

        List<CartItem> cartItems = cartDto.getCartItems().stream()
                .map(cartItemsDto -> new CartItem(null, cartItemsDto.getProductId(), cartItemsDto.getProductName(), cartItemsDto.getQuantity())).toList();
        cart.setCartItems(cartItems);

        Cart savedcart = cartRepository.save(cart);

        /*Update the amount in db

        cartItems.forEach(cartItem -> {
            productService.updateStock(UUID.fromString(orderItem.getProductId()), orderItem.getQuantity());
        });*/

        return savedcart.getId();
    }


    public void deleteCart(UUID id) {
        if (!cartRepository.existsById(id)) {
            throw new IllegalArgumentException("Cart not found for id: " + id);
        }
        cartRepository.deleteById(id);
    }

    /*
    public void confirmOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new IllegalArgumentException("Order not found for id: " + orderId));
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }*/
}
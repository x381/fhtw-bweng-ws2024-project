package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.CartDto;
import at.fhtw.bweng_ws24.model.Cart;
import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.service.CartService;
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
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> getCarts() {
        return cartService.getCarts();
    }

    @GetMapping("/{id}")
    public Cart getCart(@PathVariable UUID id) {
        return cartService.getCart(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> createCart(@RequestBody @Valid CartDto cartDto) {
        UUID cartId = cartService.createCart(cartDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Cart created successfully");
        response.put("cartId", cartId);
        return ResponseEntity
                .created(URI.create("/carts/" + cartId))
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and hasPermission(#id, 'at.fhtw.bweng_ws24.model.User', 'delete'))")
    public ResponseEntity<Cart> deleteCart(@PathVariable UUID id) {
        cartService.deleteCart(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}

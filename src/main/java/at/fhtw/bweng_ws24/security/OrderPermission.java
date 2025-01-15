package at.fhtw.bweng_ws24.security;

import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderPermission implements AccessPermission {

    private final OrderRepository orderRepository;

    public OrderPermission(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(Product.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, UUID resourceId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Order order = orderRepository.findById(resourceId).orElse(null);

        if (order == null) {
            return false;
        }

        return order.getCreatedBy().equals(userPrincipal.getId());
    }
}

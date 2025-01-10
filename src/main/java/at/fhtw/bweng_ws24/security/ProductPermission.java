package at.fhtw.bweng_ws24.security;

import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.repository.ProductRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductPermission implements AccessPermission {

    private final ProductRepository productRepository;

    public ProductPermission(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean supports(Authentication authentication, String className) {
        return className.equals(Product.class.getName());
    }

    @Override
    public boolean hasPermission(Authentication authentication, UUID resourceId) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Product product = productRepository.findById(resourceId).orElse(null);

        if (product == null) {
            return false;
        }

        return product.getCreatedBy().equals(userPrincipal.getId());
    }
}

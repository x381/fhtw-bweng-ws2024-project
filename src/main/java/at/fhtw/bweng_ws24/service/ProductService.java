package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.ProductDto;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(UUID id) {
        return productRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Product with id " + id + " not found.")
        );
    }

    public UUID createProduct(@Valid ProductDto product) {
        Product newProduct = new Product();
        newProduct.setName(product.getName());
        newProduct.setPrice(product.getPrice());
        newProduct.setDescription(product.getDescription());
        return productRepository.save(newProduct).getId();
    }
}

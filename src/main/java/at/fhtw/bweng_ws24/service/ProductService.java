package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.PostProductDto;
import at.fhtw.bweng_ws24.dto.PutProductDto;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.repository.ProductRepository;
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

    public UUID createProduct(PostProductDto product) {
        Product newProduct = new Product();
        newProduct.setName(product.getName());
//        newProduct.setImageUrl(product.getImageUrl());
        newProduct.setCategory(product.getCategory());
        newProduct.setDescription(product.getDescription());
        newProduct.setCreatedBy(UUID.fromString(product.getCreatedBy()));
        newProduct.setLastUpdatedBy(UUID.fromString(product.getCreatedBy()));
        newProduct.setStock(product.getStock());
        newProduct.setPrice(product.getPrice());
        return productRepository.save(newProduct).getId();
    }

    public void updateProduct(UUID id, PutProductDto product) {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Product with id " + id + " not found.")
        );
        existingProduct.setName(product.getName());
//        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setLastUpdatedBy(UUID.fromString(product.getLastUpdatedBy()));
        existingProduct.setStock(product.getStock());
        existingProduct.setPrice(product.getPrice());
        productRepository.save(existingProduct);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }
}

package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.PostProductDto;
import at.fhtw.bweng_ws24.dto.PutProductDto;
import at.fhtw.bweng_ws24.exception.StockNotEnoughException;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.model.ProductCategory;
import at.fhtw.bweng_ws24.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ResourceImageService resourceImageService;

    public ProductService(ProductRepository productRepository, ResourceImageService resourceImageService) {
        this.productRepository = productRepository;
        this.resourceImageService = resourceImageService;
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
        newProduct.setCategory(product.getCategory());
        newProduct.setDescription(product.getDescription());
        newProduct.setCreatedBy(UUID.fromString(product.getCreatedBy()));
        newProduct.setLastUpdatedBy(UUID.fromString(product.getCreatedBy()));
        newProduct.setPrice(product.getPrice());
        return productRepository.save(newProduct).getId();
    }

    public void updateProduct(UUID id, PutProductDto product) {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Product with id " + id + " not found.")
        );
        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setLastUpdatedBy(UUID.fromString(product.getLastUpdatedBy()));
        existingProduct.setPrice(product.getPrice());
        productRepository.save(existingProduct);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    public List<Product> getProductsByCreatedBy(UUID createdBy) {
        return productRepository.findByCreatedBy(createdBy);
    }

    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Product with id " + id + " not found.")
        );
        if (product.getImage() != null) {
            resourceImageService.deleteResourceImage(UUID.fromString(product.getImage()));
        }
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    public void updateStock(UUID productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NoSuchElementException("Product with id " + productId + " not found.")
        );
        if (product.getStock() < quantity) {
            throw new StockNotEnoughException("Not enough stock available for product with id " + productId);
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }
}
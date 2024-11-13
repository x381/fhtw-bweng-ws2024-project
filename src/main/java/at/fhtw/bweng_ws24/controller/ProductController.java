package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.PostProductDto;
import at.fhtw.bweng_ws24.dto.PutProductDto;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid PostProductDto product) {
        UUID productId = productService.createProduct(product);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product created successfully");
        response.put("productId", productId);
        return ResponseEntity
                .created(URI.create("/products/" + productId))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody @Valid PutProductDto product) {
        productService.updateProduct(id, product);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product updated successfully");
        return ResponseEntity
                .ok()
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}

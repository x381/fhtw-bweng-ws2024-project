package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.PostProductDto;
import at.fhtw.bweng_ws24.dto.PutProductDto;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.model.ProductCategory;
import at.fhtw.bweng_ws24.service.ProductService;
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

    @GetMapping(params = {"category"})
    public List<Product> getProductsByCategory(@RequestParam ProductCategory category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping(params = {"createdBy"})
    public List<Product> getProductsByCreatedBy(@RequestParam UUID createdBy) {
        return productService.getProductsByCreatedBy(createdBy);
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable UUID id) {
        return productService.getProduct(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateProduct(@PathVariable UUID id, @RequestBody @Valid PutProductDto product) {
        productService.updateProduct(id, product);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Product updated successfully");
        return ResponseEntity
                .ok()
                .body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}

package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.CreateProductDto;
import at.fhtw.bweng_ws24.dto.UpdateProductDto;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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
    public ResponseEntity<Product> createProduct(@RequestBody @Valid CreateProductDto product) {
        UUID uuid = productService.createProduct(product);
        return ResponseEntity
                .created(URI.create("/products/" + uuid))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody @Valid UpdateProductDto product) {
        productService.updateProduct(id, product);
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}

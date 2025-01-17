package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.PostProductDto;
import at.fhtw.bweng_ws24.dto.PutProductDto;
import at.fhtw.bweng_ws24.exception.StockNotEnoughException;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.model.ProductCategory;
import at.fhtw.bweng_ws24.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ResourceImageService resourceImageService;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldReturnCreatedProductId() {
        // Arrange
        PostProductDto postProductDto = new PostProductDto();
        postProductDto.setName("Laptop");
        postProductDto.setCategory(ProductCategory.ELECTRONIC_DEVICES);
        postProductDto.setDescription("A high-performance laptop");
        postProductDto.setCreatedBy(UUID.randomUUID().toString());
        postProductDto.setPrice(1200.0);

        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        UUID createdProductId = productService.createProduct(postProductDto);

        // Assert
        assertNotNull(createdProductId);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProducts_shouldReturnListOfProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Laptop", null, ProductCategory.ELECTRONIC_DEVICES, "High-performance laptop", UUID.randomUUID(), UUID.randomUUID(), 100, 1200.0, null, null),
                new Product(UUID.randomUUID(), "Vacuum Cleaner", null, ProductCategory.HOUSEHOLD_STUFF, "A powerful vacuum cleaner", UUID.randomUUID(), UUID.randomUUID(), 50, 200.0, null, null)
        );
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getProducts();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals(ProductCategory.ELECTRONIC_DEVICES, result.get(0).getCategory());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProduct_shouldReturnProductById() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Laptop", null, ProductCategory.ELECTRONIC_DEVICES, "High-performance laptop", UUID.randomUUID(), UUID.randomUUID(), 100, 1200.0, null, null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProduct(productId);

        // Assert
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProduct_shouldThrowExceptionIfNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> productService.getProduct(productId));
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void updateProduct_shouldUpdateExistingProduct() {
        // Arrange
        UUID productId = UUID.randomUUID();
        PutProductDto putProductDto = new PutProductDto();
        putProductDto.setName("Updated Laptop");
        putProductDto.setCategory(ProductCategory.ELECTRONIC_DEVICES);
        putProductDto.setDescription("Updated high-performance laptop");
        putProductDto.setLastUpdatedBy(UUID.randomUUID().toString());
        putProductDto.setPrice(1500.0);

        Product existingProduct = new Product(productId, "Laptop", null, ProductCategory.ELECTRONIC_DEVICES, "High-performance laptop", UUID.randomUUID(), UUID.randomUUID(), 100, 1200.0, null, null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        // Act
        productService.updateProduct(productId, putProductDto);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldThrowExceptionIfNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        PutProductDto putProductDto = new PutProductDto();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(productId, putProductDto));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_shouldDeleteExistingProduct() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Laptop", UUID.randomUUID().toString(), ProductCategory.ELECTRONIC_DEVICES, "High-performance laptop", UUID.randomUUID(), UUID.randomUUID(), 100, 1200.0, null, null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(productId);

        // Assert
        verify(resourceImageService, times(1)).deleteResourceImage(any(UUID.class));
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void deleteProduct_shouldThrowExceptionIfNotFound() {
        // Arrange
        UUID productId = UUID.randomUUID();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> productService.deleteProduct(productId));
        verify(productRepository, never()).deleteById(productId);
    }

    @Test
    void updateStock_shouldUpdateStockSuccessfully() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Laptop", null, ProductCategory.ELECTRONIC_DEVICES, "High-performance laptop", UUID.randomUUID(), UUID.randomUUID(), 100, 1200.0, null, null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act
        productService.updateStock(productId, 10);

        // Assert
        assertEquals(90, product.getStock());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateStock_shouldThrowExceptionIfStockNotEnough() {
        // Arrange
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "Laptop", null, ProductCategory.ELECTRONIC_DEVICES, "High-performance laptop", UUID.randomUUID(), UUID.randomUUID(), 5, 1200.0, null, null);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // Act & Assert
        assertThrows(StockNotEnoughException.class, () -> productService.updateStock(productId, 10));
        verify(productRepository, never()).save(product);
    }
}
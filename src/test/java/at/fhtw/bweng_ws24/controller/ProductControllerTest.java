package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.PostProductDto;
import at.fhtw.bweng_ws24.dto.PutProductDto;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.model.ProductCategory;
import at.fhtw.bweng_ws24.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetProducts() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Product1", null, ProductCategory.ELECTRONIC_DEVICES, "Description1", UUID.randomUUID(), UUID.randomUUID(), 100, 10.0, null, null),
                new Product(UUID.randomUUID(), "Product2", null, ProductCategory.CLOTHINGS, "Description2", UUID.randomUUID(), UUID.randomUUID(), 50, 20.0, null, null)
        );
        when(productService.getProducts()).thenReturn(products);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product1"))
                .andExpect(jsonPath("$[1].name").value("Product2"));
    }

    @Test
    void testGetProductsByCategory() throws Exception {
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Electronics1", null, ProductCategory.ELECTRONIC_DEVICES, "Description1", UUID.randomUUID(), UUID.randomUUID(), 100, 10.0, null, null),
                new Product(UUID.randomUUID(), "Electronics2", null, ProductCategory.ELECTRONIC_DEVICES, "Description2", UUID.randomUUID(), UUID.randomUUID(), 50, 20.0, null, null)
        );
        when(productService.getProductsByCategory(ProductCategory.ELECTRONIC_DEVICES)).thenReturn(products);

        mockMvc.perform(get("/products").param("category", "ELECTRONIC_DEVICES"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].category").value("ELECTRONIC_DEVICES"))
                .andExpect(jsonPath("$[1].category").value("ELECTRONIC_DEVICES"));
    }

    @Test
    void testGetProductsByCreatedBy() throws Exception {
        UUID createdBy = UUID.randomUUID();
        List<Product> products = Arrays.asList(
                new Product(UUID.randomUUID(), "Product1", null, ProductCategory.ELECTRONIC_DEVICES, "Description1", createdBy, UUID.randomUUID(), 100, 10.0, null, null),
                new Product(UUID.randomUUID(), "Product2", null, ProductCategory.CLOTHINGS, "Description2", createdBy, UUID.randomUUID(), 50, 20.0, null, null)
        );
        when(productService.getProductsByCreatedBy(createdBy)).thenReturn(products);

        mockMvc.perform(get("/products").param("createdBy", createdBy.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].createdBy").value(createdBy.toString()))
                .andExpect(jsonPath("$[1].createdBy").value(createdBy.toString()));
    }

    @Test
    void testGetProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = new Product(productId, "TestProduct", null, ProductCategory.ELECTRONIC_DEVICES, "Description", UUID.randomUUID(), UUID.randomUUID(), 100, 10.0, null, null);
        when(productService.getProduct(productId)).thenReturn(product);

        mockMvc.perform(get("/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").value("TestProduct"));
    }

    @Test
    void testCreateProduct() throws Exception {
        PostProductDto postProductDto = new PostProductDto();
        postProductDto.setName("New Product");
        postProductDto.setCategory(ProductCategory.ELECTRONIC_DEVICES);
        postProductDto.setDescription("New Description");
        postProductDto.setCreatedBy(UUID.randomUUID().toString());
        postProductDto.setPrice(99.99);

        UUID productId = UUID.randomUUID();
        when(productService.createProduct(any(PostProductDto.class))).thenReturn(productId);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postProductDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/" + productId))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.productId").value(productId.toString()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        PutProductDto putProductDto = new PutProductDto();
        putProductDto.setName("Updated Product");
        putProductDto.setDescription("Updated Description");
        putProductDto.setLastUpdatedBy(UUID.randomUUID().toString());
        putProductDto.setPrice(149.99);

        mockMvc.perform(put("/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putProductDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product updated successfully"));

        verify(productService, times(1)).updateProduct(eq(productId), any(PutProductDto.class));
    }

    @Test
    void testDeleteProduct() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(delete("/products/{id}", productId))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }
}

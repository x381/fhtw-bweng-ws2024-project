package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.ResourceImageDto;
import at.fhtw.bweng_ws24.model.ResourceImage;
import at.fhtw.bweng_ws24.service.ResourceImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ResourceImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ResourceImageService resourceImageService;

    @InjectMocks
    private ResourceImageController resourceImageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(resourceImageController).build();
    }

    @Test
    void testUploadProfilePicture() throws Exception {
        UUID userId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        ResourceImageDto resourceImageDto = new ResourceImageDto();
        resourceImageDto.setId(UUID.randomUUID());

        when(resourceImageService.uploadProfilePicture(any(UUID.class), any(MultipartFile.class))).thenReturn(resourceImageDto);

        mockMvc.perform(multipart("/images/profile-picture/" + userId).file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(resourceImageService).uploadProfilePicture(eq(userId), any(MultipartFile.class));
    }

    @Test
    void testUploadProductPicture() throws Exception {
        UUID productId = UUID.randomUUID();
        MockMultipartFile file = new MockMultipartFile("file", "product.jpg", MediaType.IMAGE_JPEG_VALUE, "product image content".getBytes());
        ResourceImageDto resourceImageDto = new ResourceImageDto();
        resourceImageDto.setId(UUID.randomUUID());

        when(resourceImageService.uploadProductPicture(any(UUID.class), any(MultipartFile.class))).thenReturn(resourceImageDto);

        mockMvc.perform(multipart("/images/product-picture/" + productId).file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        verify(resourceImageService).uploadProductPicture(eq(productId), any(MultipartFile.class));
    }

    @Test
    void testDeleteProfilePicture() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/images/profile-picture/" + userId))
                .andExpect(status().isNoContent());

        verify(resourceImageService).deleteProfilePicture(userId);
    }

    @Test
    void testDeleteProductPicture() throws Exception {
        UUID productId = UUID.randomUUID();

        mockMvc.perform(delete("/images/product-picture/" + productId))
                .andExpect(status().isNoContent());

        verify(resourceImageService).deleteProductPicture(productId);
    }

    @Test
    void testRetrieve() throws Exception {
        UUID imageId = UUID.randomUUID();
        ResourceImage resourceImage = new ResourceImage();
        resourceImage.setContentType(MediaType.IMAGE_JPEG_VALUE);

        Resource mockResource = new ByteArrayResource("test image content".getBytes());

        when(resourceImageService.findById(imageId)).thenReturn(resourceImage);
        when(resourceImageService.asResource(resourceImage)).thenReturn(mockResource);

        mockMvc.perform(get("/images/" + imageId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes("test image content".getBytes()));

        verify(resourceImageService).findById(imageId);
        verify(resourceImageService).asResource(resourceImage);
    }
}

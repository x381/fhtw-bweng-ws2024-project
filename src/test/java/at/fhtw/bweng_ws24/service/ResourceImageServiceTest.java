package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.ResourceImageDto;
import at.fhtw.bweng_ws24.mapper.ResourceImageMapper;
import at.fhtw.bweng_ws24.model.Product;
import at.fhtw.bweng_ws24.model.ResourceImage;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.repository.ResourceImageRepository;
import at.fhtw.bweng_ws24.storage.FileStorage;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceImageServiceTest {

    @Mock
    private ResourceImageRepository resourceImageRepository;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private ResourceImageMapper resourceImageMapper;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ResourceImageService resourceImageService;

    private UUID userId;
    private UUID productId;
    private UUID imageId;
    private MultipartFile multipartFile;
    private ResourceImage resourceImage;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        productId = UUID.randomUUID();
        imageId = UUID.randomUUID();

        resourceImage = new ResourceImage();
        resourceImage.setId(imageId);
        resourceImage.setExternalId("externalId");
        resourceImage.setContentType("image/jpeg");
        resourceImage.setName("test-image.jpg");
    }

    @Test
    void save_shouldSaveResourceImage() {
        when(resourceImageRepository.save(resourceImage)).thenReturn(resourceImage);

        ResourceImage savedImage = resourceImageService.save(resourceImage);

        assertNotNull(savedImage);
        assertEquals(resourceImage.getId(), savedImage.getId());
        verify(resourceImageRepository, times(1)).save(resourceImage);
    }

    @Test
    void uploadProfilePicture_shouldUploadAndReturnResourceImageDto() {
        // Arrange
        ResourceImageDto savedResourceImageDto = new ResourceImageDto();
        savedResourceImageDto.setId(UUID.fromString("4c1ce0e1-de21-4b1e-8ff7-e3fa37bdb239"));
        User user = new User();
        multipartFile = mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(userService.getUser(userId)).thenReturn(user);
        when(fileStorage.upload(multipartFile)).thenReturn("externalId");
        when(resourceImageRepository.save(any(ResourceImage.class))).thenReturn(resourceImage);
        when(resourceImageMapper.toDto(resourceImage)).thenReturn(savedResourceImageDto);

        // Act
        ResourceImageDto result = resourceImageService.uploadProfilePicture(userId, multipartFile);

        // Assert
        assertNotNull(result);
        verify(fileStorage, times(1)).upload(multipartFile);
        verify(resourceImageRepository, times(1)).save(any(ResourceImage.class));
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void uploadProfilePicture_shouldUploadAndReturnResourceImageDtoAndDeleteOldPicture() {
        // Arrange
        ResourceImageDto savedResourceImageDto = new ResourceImageDto();
        savedResourceImageDto.setId(UUID.fromString("4c1ce0e1-de21-4b1e-8ff7-e3fa37bdb239"));
        User user = new User();
        user.setImage(imageId.toString());
        multipartFile = mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(userService.getUser(userId)).thenReturn(user);
        when(fileStorage.upload(multipartFile)).thenReturn("externalId");
        when(resourceImageRepository.save(any(ResourceImage.class))).thenReturn(resourceImage);
        when(resourceImageMapper.toDto(resourceImage)).thenReturn(savedResourceImageDto);
        when(resourceImageRepository.findById(imageId)).thenReturn(Optional.of(resourceImage));

        // Act
        ResourceImageDto result = resourceImageService.uploadProfilePicture(userId, multipartFile);

        // Assert
        assertNotNull(result);
        verify(fileStorage, times(1)).upload(multipartFile);
        verify(resourceImageRepository, times(1)).save(any(ResourceImage.class));
        verify(userService, times(1)).saveUser(user);
        verify(fileStorage, times(1)).delete(resourceImage.getExternalId());
        verify(resourceImageRepository, times(1)).delete(resourceImage);
    }

    @Test
    void uploadProductPicture_shouldUploadAndReturnResourceImageDto() {
        // Arrange
        ResourceImageDto savedResourceImageDto = new ResourceImageDto();
        savedResourceImageDto.setId(UUID.fromString("4c1ce0e1-de21-4b1e-8ff7-e3fa37bdb239"));
        Product product = new Product();
        multipartFile = mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(productService.getProduct(productId)).thenReturn(product);
        when(fileStorage.upload(multipartFile)).thenReturn("externalId");
        when(resourceImageRepository.save(any(ResourceImage.class))).thenReturn(resourceImage);
        when(resourceImageMapper.toDto(resourceImage)).thenReturn(savedResourceImageDto);

        // Act
        ResourceImageDto result = resourceImageService.uploadProductPicture(productId, multipartFile);

        // Assert
        assertNotNull(result);
        verify(fileStorage, times(1)).upload(multipartFile);
        verify(resourceImageRepository, times(1)).save(any(ResourceImage.class));
        verify(productService, times(1)).saveProduct(product);
    }

    @Test
    void uploadProductPicture_shouldUploadAndReturnResourceImageDtoAndDeleteOldPicture() {
        // Arrange
        ResourceImageDto savedResourceImageDto = new ResourceImageDto();
        savedResourceImageDto.setId(UUID.fromString("4c1ce0e1-de21-4b1e-8ff7-e3fa37bdb239"));
        Product product = new Product();
        product.setImage(imageId.toString());
        multipartFile = mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(multipartFile.getOriginalFilename()).thenReturn("test-image.jpg");
        when(productService.getProduct(productId)).thenReturn(product);
        when(fileStorage.upload(multipartFile)).thenReturn("externalId");
        when(resourceImageRepository.save(any(ResourceImage.class))).thenReturn(resourceImage);
        when(resourceImageMapper.toDto(resourceImage)).thenReturn(savedResourceImageDto);
        when(resourceImageRepository.findById(imageId)).thenReturn(Optional.of(resourceImage));

        // Act
        ResourceImageDto result = resourceImageService.uploadProductPicture(productId, multipartFile);

        // Assert
        assertNotNull(result);
        verify(fileStorage, times(1)).upload(multipartFile);
        verify(resourceImageRepository, times(1)).save(any(ResourceImage.class));
        verify(productService, times(1)).saveProduct(product);
        verify(fileStorage, times(1)).delete(resourceImage.getExternalId());
        verify(resourceImageRepository, times(1)).delete(resourceImage);
    }

    @Test
    void deleteProfilePicture_shouldDeleteUserProfilePicture() {
        // Arrange
        User user = new User();
        user.setImage(imageId.toString());
        when(userService.getUser(userId)).thenReturn(user);
        when(resourceImageRepository.findById(imageId)).thenReturn(Optional.of(resourceImage));

        // Act
        resourceImageService.deleteProfilePicture(userId);

        // Assert
        verify(fileStorage, times(1)).delete(resourceImage.getExternalId());
        verify(resourceImageRepository, times(1)).delete(resourceImage);
        verify(userService, times(1)).saveUser(user);
        assertNull(user.getImage());
    }

    @Test
    void deleteProductPicture_shouldDeleteProductPicture() {
        // Arrange
        Product product = new Product();
        product.setImage(imageId.toString());
        when(productService.getProduct(productId)).thenReturn(product);
        when(resourceImageRepository.findById(imageId)).thenReturn(Optional.of(resourceImage));

        // Act
        resourceImageService.deleteProductPicture(productId);

        // Assert
        verify(fileStorage, times(1)).delete(resourceImage.getExternalId());
        verify(resourceImageRepository, times(1)).delete(resourceImage);
        verify(productService, times(1)).saveProduct(product);
        assertNull(product.getImage());
    }

    @Test
    void deleteResourceImage_shouldDeleteResourceImage() {
        // Arrange
        when(resourceImageRepository.findById(imageId)).thenReturn(Optional.of(resourceImage));

        // Act
        resourceImageService.deleteResourceImage(imageId);

        // Assert
        verify(fileStorage, times(1)).delete(resourceImage.getExternalId());
        verify(resourceImageRepository, times(1)).delete(resourceImage);
    }

    @Test
    void findById_shouldReturnResourceImage() {
        // Arrange
        when(resourceImageRepository.findById(imageId)).thenReturn(Optional.of(resourceImage));

        // Act
        ResourceImage result = resourceImageService.findById(imageId);

        // Assert
        assertNotNull(result);
        assertEquals(resourceImage.getId(), result.getId());
        verify(resourceImageRepository, times(1)).findById(imageId);
    }

    @Test
    void findById_shouldThrowExceptionIfNotFound() {
        // Arrange
        when(resourceImageRepository.findById(imageId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> resourceImageService.findById(imageId));
    }

    @Test
    void asResource_shouldReturnInputStreamResource() {
        // Arrange
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        when(fileStorage.download(resourceImage.getExternalId())).thenReturn(inputStream);

        // Act
        Resource resource = resourceImageService.asResource(resourceImage);

        // Assert
        assertNotNull(resource);
        assertTrue(resource instanceof InputStreamResource);
        verify(fileStorage, times(1)).download(resourceImage.getExternalId());
    }
}
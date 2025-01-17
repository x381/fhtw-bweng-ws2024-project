package at.fhtw.bweng_ws24.storage;

import at.fhtw.bweng_ws24.property.MinioProperties;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MinioConfigTest {

    @Test
    void testMinioClientBean() {
        // Mock MinioProperties
        MinioProperties minioProperties = Mockito.mock(MinioProperties.class);
        Mockito.when(minioProperties.getUrl()).thenReturn("http://localhost");
        Mockito.when(minioProperties.getPort()).thenReturn(9000);
        Mockito.when(minioProperties.getUser()).thenReturn("user");
        Mockito.when(minioProperties.getPassword()).thenReturn("password");

        // Create MinioConfig instance and test minioClient bean creation
        MinioConfig minioConfig = new MinioConfig(minioProperties);

        // Assert that MinioClient bean is not null
        assertNotNull(minioConfig.minioClient());
    }
}
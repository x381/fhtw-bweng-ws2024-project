package at.fhtw.bweng_ws24.storage;

import at.fhtw.bweng_ws24.exception.FileException;
import at.fhtw.bweng_ws24.property.MinioProperties;
import io.minio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MinioStorageTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @InjectMocks
    private MinioStorage minioStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpload_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        String bucketName = "test-bucket";
        when(minioProperties.getBucket()).thenReturn(bucketName);

        String uuid = minioStorage.upload(file);

        assertNotNull(uuid);
        verify(minioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testUpload_Failure() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test content".getBytes());
        when(minioProperties.getBucket()).thenReturn("test-bucket");
        doThrow(new RuntimeException("Upload failed")).when(minioClient).putObject(any(PutObjectArgs.class));

        assertThrows(FileException.class, () -> minioStorage.upload(file));
    }

    @Test
    void testDownload_Success() throws Exception {
        String fileId = "test-id";
        String bucketName = "test-bucket";
        byte[] testContent = "test content".getBytes();
        GetObjectResponse mockResponse = mock(GetObjectResponse.class);

        when(minioProperties.getBucket()).thenReturn(bucketName);
        when(minioClient.getObject(any(GetObjectArgs.class))).thenReturn(mockResponse);
        when(mockResponse.readAllBytes()).thenReturn(testContent);

        InputStream result = minioStorage.download(fileId);

        assertNotNull(result);
        assertArrayEquals(testContent, result.readAllBytes());
        verify(minioClient, times(1)).getObject(any(GetObjectArgs.class));
    }

    @Test
    void testDownload_Failure() throws Exception {
        String fileId = "test-id";
        when(minioProperties.getBucket()).thenReturn("test-bucket");
        doThrow(new RuntimeException("Download failed")).when(minioClient).getObject(any(GetObjectArgs.class));

        assertThrows(FileException.class, () -> minioStorage.download(fileId));
    }

    @Test
    void testDelete_Success() throws Exception {
        String fileId = "test-id";
        String bucketName = "test-bucket";
        when(minioProperties.getBucket()).thenReturn(bucketName);

        minioStorage.delete(fileId);

        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    void testDelete_Failure() throws Exception {
        String fileId = "test-id";
        when(minioProperties.getBucket()).thenReturn("test-bucket");
        doThrow(new RuntimeException("Delete failed")).when(minioClient).removeObject(any(RemoveObjectArgs.class));

        assertThrows(FileException.class, () -> minioStorage.delete(fileId));
    }
}

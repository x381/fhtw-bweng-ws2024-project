package at.fhtw.bweng_ws24.storage;

import at.fhtw.bweng_ws24.exception.FileException;
import at.fhtw.bweng_ws24.property.MinioProperties;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class MinioStorage implements FileStorage {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioStorage(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @Override
    public String upload(MultipartFile file) {
        String uuid = UUID.randomUUID().toString();

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(uuid)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (Exception e) {
            throw new FileException("Upload failed for file with uuid=" + uuid, e);
        }
        return uuid;
    }

    @Override
    public InputStream download(String id) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(id)
                            .build()
            );
        } catch (Exception e) {
            throw new FileException("Download failed for id=" + id, e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            minioClient.removeObject(
                    io.minio.RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(id)
                            .build()
            );
        } catch (Exception e) {
            throw new FileException("Delete failed for id=" + id, e);
        }
    }
}

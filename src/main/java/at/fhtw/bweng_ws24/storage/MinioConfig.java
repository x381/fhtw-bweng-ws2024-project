package at.fhtw.bweng_ws24.storage;

import at.fhtw.bweng_ws24.property.MinioProperties;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    private final MinioProperties minioProperties;

    public MinioConfig(MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .credentials(
                        minioProperties.getUser(),
                        minioProperties.getPassword()
                )
                .endpoint(minioProperties.getUrl(),
                        minioProperties.getPort(),
                        minioProperties.getUrl().contains("https"))
                .build();
    }
}

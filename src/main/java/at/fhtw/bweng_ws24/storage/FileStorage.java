package at.fhtw.bweng_ws24.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Component
public interface FileStorage {
    String upload(MultipartFile file);
    InputStream download(String id);
    void delete(String id);
}

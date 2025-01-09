package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.ResourceImageDto;
import at.fhtw.bweng_ws24.mapper.ResourceImageMapper;
import at.fhtw.bweng_ws24.model.ResourceImage;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.repository.ResourceImageRepository;
import at.fhtw.bweng_ws24.storage.FileStorage;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class ResourceImageService {

    private final ResourceImageRepository resourceImageRepository;
    private final FileStorage fileStorage;
    private final ResourceImageMapper resourceImageMapper;
    private final UserService userService;

    public ResourceImageService(ResourceImageRepository resourceImageRepository, FileStorage fileStorage, ResourceImageMapper resourceImageMapper, UserService userService) {
        this.resourceImageRepository = resourceImageRepository;
        this.fileStorage = fileStorage;
        this.resourceImageMapper = resourceImageMapper;
        this.userService = userService;
    }

    public ResourceImage save(ResourceImage resourceImage) {
        return resourceImageRepository.save(resourceImage);
    }

    public ResourceImageDto upload(UUID userId, MultipartFile file) {
        User user = userService.getUser(userId);
        if (user.getImage() != null) {
            ResourceImage resourceImage = findById(UUID.fromString(user.getImage()));
            fileStorage.delete(resourceImage.getExternalId());
            resourceImageRepository.delete(resourceImage);
        }

        String externalId = fileStorage.upload(file);

        ResourceImage resourceImage = new ResourceImage();
        resourceImage.setExternalId(externalId);
        resourceImage.setContentType(file.getContentType());
        resourceImage.setName(file.getOriginalFilename());

        ResourceImageDto resourceImageDto = resourceImageMapper.toDto(resourceImageRepository.save(resourceImage));

        user.setImage(resourceImageDto.getId().toString());
        userService.saveUser(user);
        return resourceImageDto;
    }

    public ResourceImage findById(UUID id) {
        return resourceImageRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Resource asResource(ResourceImage resourceImage) {
        InputStream stream = fileStorage.download(resourceImage.getExternalId());
        return new InputStreamResource(stream);
    }
}

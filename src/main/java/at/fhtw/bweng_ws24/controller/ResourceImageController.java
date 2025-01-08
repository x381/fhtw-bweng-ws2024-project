package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.ResourceImageDto;
import at.fhtw.bweng_ws24.model.ResourceImage;
import at.fhtw.bweng_ws24.service.ResourceImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/images")
public class ResourceImageController {

    private final ResourceImageService resourceImageService;

    public ResourceImageController(ResourceImageService resourceImageService) {
        this.resourceImageService = resourceImageService;
    }

    @PostMapping("")
    public ResourceImageDto upload(@RequestParam("file") MultipartFile file) {
        return resourceImageService.upload(file);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> retrieve(@PathVariable("id") UUID id) {
        ResourceImage resourceImage = resourceImageService.findById(id);
        Resource resource = resourceImageService.asResource(resourceImage);
        MediaType mediaType = MediaType.parseMediaType(resourceImage.getContentType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}

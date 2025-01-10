package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.ResourceImageDto;
import at.fhtw.bweng_ws24.model.ResourceImage;
import at.fhtw.bweng_ws24.service.ResourceImageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/images")
public class ResourceImageController {

    private final ResourceImageService resourceImageService;

    public ResourceImageController(ResourceImageService resourceImageService) {
        this.resourceImageService = resourceImageService;
    }

    @PostMapping("/profile-picture/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and hasPermission(#id, 'at.fhtw.bweng_ws24.model.User', 'update'))")
    public ResourceImageDto uploadProfilePicture(@PathVariable("userId") UUID userId, @RequestParam("file") MultipartFile file) {
        return resourceImageService.uploadProfilePicture(userId, file);
    }

    @PostMapping("/product-picture/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and hasPermission(#id, 'at.fhtw.bweng_ws24.model.Product', 'update'))")
    public ResourceImageDto uploadProductPicture(@PathVariable("productId") UUID productId, @RequestParam("file") MultipartFile file) {
        return resourceImageService.uploadProductPicture(productId, file);
    }

    @DeleteMapping("/profile-picture/{userId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and hasPermission(#id, 'at.fhtw.bweng_ws24.model.User', 'delete'))")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable("userId") UUID userId) {
        resourceImageService.deleteProfilePicture(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/product-picture/{productId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and hasPermission(#id, 'at.fhtw.bweng_ws24.model.Product', 'delete'))")
    public ResponseEntity<?> deleteProductPicture(@PathVariable("productId") UUID productId) {
        resourceImageService.deleteProductPicture(productId);
        return ResponseEntity.noContent().build();
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

package at.fhtw.bweng_ws24.mapper;

import at.fhtw.bweng_ws24.dto.ResourceImageDto;
import at.fhtw.bweng_ws24.model.ResourceImage;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceImageMapperTest {

    private final ResourceImageMapper resourceImageMapper = Mappers.getMapper(ResourceImageMapper.class);

    @Test
    void shouldMapResourceImageToDto() {
        // Create a ResourceImage object
        ResourceImage resourceImage = new ResourceImage();
        UUID id = UUID.randomUUID();
        resourceImage.setId(id);

        // Map ResourceImage to ResourceImageDto
        ResourceImageDto resourceImageDto = resourceImageMapper.toDto(resourceImage);

        // Verify the mapping
        assertThat(resourceImageDto).isNotNull();
        assertThat(resourceImageDto.getId()).isEqualTo(resourceImage.getId());
    }

    @Test
    void shouldMapDtoToResourceImage() {
        // Create a ResourceImageDto object
        ResourceImageDto resourceImageDto = new ResourceImageDto();
        UUID id = UUID.randomUUID();
        resourceImageDto.setId(id);

        // Map ResourceImageDto to ResourceImage
        ResourceImage resourceImage = resourceImageMapper.toEntity(resourceImageDto);

        // Verify the mapping
        assertThat(resourceImage).isNotNull();
        assertThat(resourceImage.getId()).isEqualTo(resourceImageDto.getId());
    }
}
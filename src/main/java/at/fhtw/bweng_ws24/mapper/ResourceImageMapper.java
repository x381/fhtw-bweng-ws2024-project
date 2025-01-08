package at.fhtw.bweng_ws24.mapper;

import at.fhtw.bweng_ws24.dto.ResourceImageDto;
import at.fhtw.bweng_ws24.model.ResourceImage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ResourceImageMapper {

    ResourceImageDto toDto(ResourceImage resourceImage);
    ResourceImage toEntity(ResourceImageDto resourceImageDto);
}

package at.fhtw.bweng_ws24.mapper;

import at.fhtw.bweng_ws24.dto.UserResponseDto;
import at.fhtw.bweng_ws24.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);
}

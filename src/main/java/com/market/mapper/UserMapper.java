package com.market.mapper;

import com.market.dto.RegisterRequest;
import com.market.dto.UserDto;
import com.market.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    User userEntityToDto(UserDto user);
    UserDto userDtoToEntity(User user);

    User registerDataToEntity(RegisterRequest user);
}
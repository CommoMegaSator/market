package com.task.mapper;

import com.task.dto.RegisterRequest;
import com.task.dto.UserDto;
import com.task.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userEntityToDto(User user);
    User userDtoToEntity(UserDto user);

    User registerDataToEntity(RegisterRequest user);

    List<UserDto> entityListToDtoList(List<User> users);
    List<User> dtoListToEntityList(List<UserDto> users);
}
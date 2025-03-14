package com.task.mapper;

import com.task.dto.AttributeDto;
import com.task.dto.DocumentDto;
import com.task.entity.Document;
import com.task.entity.DocumentAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    @Mapping(target = "attributes", source = "attributes")
    @Mapping(target = "owners", ignore = true)
    DocumentDto toDto(Document document);

    @Mapping(target = "owners", ignore = true)
    Document toEntity(DocumentDto document);

    @Mapping(source = "attributeKey", target = "key")
    @Mapping(source = "attributeValue", target = "value")
    AttributeDto toDto(DocumentAttribute attribute);

    @Mapping(source = "key", target = "attributeKey")
    @Mapping(source = "value", target = "attributeValue")
    DocumentAttribute toEntity(AttributeDto attributeDto);

    List<AttributeDto> mapAttributes(List<DocumentAttribute> attributes);
}

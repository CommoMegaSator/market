package com.task.mapper;

import com.task.dto.AttributeDto;
import com.task.dto.DocumentDto;
import com.task.entity.Document;
import com.task.entity.DocumentAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);

    DocumentDto toDto(Document document);


    Document toEntity(DocumentDto document);

    @Mapping(source = "attributeKey", target = "key")
    @Mapping(source = "attributeValue", target = "value")
    AttributeDto toDto(DocumentAttribute attribute);
}

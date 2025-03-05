package com.market.mapper;

import com.market.dto.CategoryDto;
import com.market.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parentId", source = "parent.id")
    CategoryDto toDto(Category category);

    @Mapping(target = "parent", ignore = true)
    Category toEntity(CategoryDto dto);
}

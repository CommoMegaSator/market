package com.market.mapper;

import com.market.dto.ProductDto;
import com.market.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.id")
    ProductDto toDto(Product product);

    @Mapping(target = "category", ignore = true) // встановимо вручну
    Product toEntity(ProductDto dto);
}
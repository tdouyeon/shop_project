package com.shop.modelmapper;

import com.shop.dto.CategoryDto;
import com.shop.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static CategoryDto convertToDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }

    public static List<CategoryDto> convertToDtoList(List<Category> categoryList) {
        return categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }
}

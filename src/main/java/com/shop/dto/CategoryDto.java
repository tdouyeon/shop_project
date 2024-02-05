package com.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private List<String> subcategories; // 서브 카테고리

    public CategoryDto() {
    }
}

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
    private Long parent_category_id;

    public CategoryDto() {
    }
}

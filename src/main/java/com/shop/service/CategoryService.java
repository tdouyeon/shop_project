package com.shop.service;

import com.shop.dto.CategoryDto;
import com.shop.entity.Category;
import com.shop.modelmapper.CategoryMapper;
import com.shop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public void makeCategory() {
        List<Category> category = categoryRepository.findAll();
        if(category.isEmpty() || category.get(0) == null) {
            String [] cy = {"bag","clothing","accessories","headwear"};
            String [] bag_cy = {"crossbag","shoulderbag"};
            String [] clothing_cy = {"top","dress","bottom","outer"};
            String [] accessories_cy = {"necklace","ring","bracelet","earrings","keytring","acc"};
            String [] headwear_cy = {"hairpin","hairband"};
            for(int i =0; i< cy.length; i++) {
                categoryRepository.save(Category.createCategory(cy[i]));
            }
            for(int i =0; i < bag_cy.length; i++) {
                categoryRepository.save(Category.createCategory(bag_cy[i],1));
                categoryRepository.save(Category.createCategory(headwear_cy[i], 4));
            }
            for(int i =0; i< clothing_cy.length ; i++) {
                categoryRepository.save(Category.createCategory(clothing_cy[i], 2));
            }
            for(int i =0; i< accessories_cy.length; i++) {
                categoryRepository.save(Category.createCategory(accessories_cy[i], 3));
            }
        }
    }

    public List<CategoryDto> findCategory(String name) {
        Optional<Category> parent = categoryRepository.findByName(name);
        int num = parent.get().getId().intValue();
        List<Category> categoryList = categoryRepository.findByParentCategoryIdIsNotNull();
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for ( Category category : categoryList) {
            if(category.getParentCategoryId() == num) {
                categoryDtoList.add(CategoryMapper.convertToDto(category));
            }
        }
        return categoryDtoList;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getCategory () {
        List<Category> categoryList = categoryRepository.findByParentCategoryIdIsNull();
        return CategoryMapper.convertToDtoList(categoryList);
    }
}

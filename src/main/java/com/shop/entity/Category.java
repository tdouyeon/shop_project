
package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "category")
public class Category {
    @Id //기본키
    @Column(name="category_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동을 1씩 증가
    private Long id;

    private String name;

    @Column(name = "parent_category_id")
    private Long parentCategoryId;
    public static Category createCategory(String name){
        Category category = new Category();
        category.setName(name);
        return category;
    }
    public static Category createCategory(String name, int parent_category_id){
        Category category = new Category();
        category.setName(name);
        category.setParentCategoryId((long)parent_category_id);
        return category;
    }
}



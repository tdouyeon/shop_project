
package com.shop.entity;

import com.shop.dto.NoticeDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;

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

    private List<String> subcategories; // 서브 카테고리

    public static Category createCategory(String name, List<String> subcategories){
        Category category = new Category();
        category.setName(name);
        category.setSubcategories(subcategories);
        return category;
    }
}



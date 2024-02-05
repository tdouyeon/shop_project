package com.shop.repository;

import com.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // name으로 category 찾기
    Optional<Category> findByName(String name);

    Optional<Category> findById(Long id);


    // 부모가 없는 즉 최상위 카테고리만 가져옴
    List<Category> findByParentCategoryIdIsNull();

    // parentCategoryId가 null이 아닌 경우만 조회
    List<Category> findByParentCategoryIdIsNotNull();

}

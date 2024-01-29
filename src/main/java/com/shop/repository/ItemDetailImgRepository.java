package com.shop.repository;

import com.shop.entity.ItemDetailImg;
import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDetailImgRepository extends JpaRepository<ItemDetailImg, Long> {
    List<ItemDetailImg> findByItemIdOrderByIdAsc(Long itemId);

    ItemImg findByItemId(Long itemId);
}

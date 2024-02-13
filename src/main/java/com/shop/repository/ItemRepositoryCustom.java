package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Category;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable, Long category_id);
    Page<MainItemDto> getMainItemPageCategorys(ItemSearchDto itemSearchDto, Pageable pageable, List<Category> categoryList);

    Page<MainItemDto> getAllItems(ItemSearchDto itemSearchDto, Pageable pageable);


}

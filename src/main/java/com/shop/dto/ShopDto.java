package com.shop.dto;

import com.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopDto {
    private Item item;
    private ItemAverageRatingDto itemAverageRatingDto;

    public ShopDto(Item item, ItemAverageRatingDto itemAverageRatingDto) {
        this.item = item;
        this.itemAverageRatingDto = itemAverageRatingDto;
    }
}

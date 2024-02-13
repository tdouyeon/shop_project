package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemAverageRatingDto {
    private Long itemId;
    private String itemName;
    private Double averageRating;
    private Long reviewCount;

    // 생성자, 게터, 세터 등 필요한 메소드들 추가

    public ItemAverageRatingDto(Long itemId, String itemName, Double averageRating, Long reviewCount) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
    }
}

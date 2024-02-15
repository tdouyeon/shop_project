package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
    private Long id;
    private String itemNm;
    private String imgUrl;
    private Integer price;
    private boolean liked;
    @QueryProjection //Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록  활용
    public MainItemDto(Long id, String itemNm, String imgUrl, Integer price, boolean liked){
        this.id = id;
        this.itemNm = itemNm;
        this.imgUrl = imgUrl;
        this.price = price;
        this.liked = liked;
    }

    @QueryProjection //Querydsl 결과 조회 시 MainItemDto 객체로 바로 오도록  활용
    public MainItemDto(Long id, String itemNm, String imgUrl, Integer price){
        this.id = id;
        this.itemNm = itemNm;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}

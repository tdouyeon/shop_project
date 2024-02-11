package com.shop.dto;


import com.shop.entity.Item;
import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewFormDto {
    private Long id;
    private Member member;
    private Item item;
    private int rating;
    private String comment;
}

package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class reviewFormDto {
    private Long id;
    private Long user_id;
    private Long product_id;
    private int rating;
    private String comment;
}

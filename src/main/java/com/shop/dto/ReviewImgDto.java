package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
}

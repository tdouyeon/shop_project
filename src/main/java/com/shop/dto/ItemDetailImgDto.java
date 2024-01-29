package com.shop.dto;

import com.shop.entity.ItemDetailImg;
import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemDetailImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemDetailImgDto of(ItemDetailImg itemDetailImg){
        return modelMapper.map(itemDetailImg, ItemDetailImgDto.class);
    }
}

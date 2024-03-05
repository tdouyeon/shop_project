package com.shop.modelmapper;

import com.shop.dto.ReviewImgDto;
import com.shop.entity.ReviewImg;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewImgMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static List<ReviewImgDto> convertToDtoList(List<ReviewImg> reviewImgList) {
        return reviewImgList.stream()
                .map(reviewImg -> modelMapper.map(reviewImg, ReviewImgDto.class))
                .collect(Collectors.toList());
    }
}

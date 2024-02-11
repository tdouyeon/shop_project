package com.shop.modelmapper;

import com.shop.dto.ReviewFormDto;
import com.shop.entity.Review;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static ReviewFormDto reviewFormDto(Review review) {
        return modelMapper.map(review, ReviewFormDto.class);
    }
    public static Review convertToEntity(ReviewFormDto reviewFormDto) {
        return modelMapper.map(reviewFormDto, Review.class);
    }

    public static List<ReviewFormDto> convertToDtoList(List<Review> reviewList) {
        return reviewList.stream()
                .map(review -> modelMapper.map(review, ReviewFormDto.class))
                .collect(Collectors.toList());
    }
}

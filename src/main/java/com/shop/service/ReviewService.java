package com.shop.service;

import com.shop.dto.ReviewFormDto;
import com.shop.entity.*;
import com.shop.modelmapper.ReviewMapper;
import com.shop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final MemberService memberService;
    private final ItemService itemService;
    private final ReviewImgService reviewImgService;
    private final ReviewRepository reviewRepository;

    public void saveReview(ReviewFormDto reviewFormDto, Long orderItemId, List<MultipartFile> reviewImgFileList, Principal principal)
            throws Exception{
        Review review =  ReviewMapper.convertToEntity(reviewFormDto);
        Member member = memberService.giveMember(memberService.checkEmail(principal));
        Long itemId = itemService.getItemFormDto(orderItemId);
        Item item = itemService.giveItem(itemId);
        review.setMember(member);
        review.setItem(item);
        reviewRepository.save(review);

        //이미지 등록
        for(int i =0;i<reviewImgFileList.size();i++){
            ReviewImg reviewImg = new ReviewImg();
            reviewImg.setReview(review);
            reviewImgService.saveReview(reviewImg,reviewImgFileList);
        }
        itemService.changeReviewStatus(orderItemId);
    }
}

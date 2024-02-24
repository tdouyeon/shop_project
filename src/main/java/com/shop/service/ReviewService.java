package com.shop.service;

import com.shop.dto.ReviewFormDto;
import com.shop.dto.ReviewImgDto;
import com.shop.entity.*;
import com.shop.modelmapper.ReviewImgMapper;
import com.shop.modelmapper.ReviewMapper;
import com.shop.repository.ReviewImgRepository;
import com.shop.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final MemberService memberService;
    private final ItemService itemService;
    private final ReviewImgService reviewImgService;
    private final ReviewRepository reviewRepository;
    private final ReviewImgRepository reviewImgRepository;

    public void saveReview(ReviewFormDto reviewFormDto, Long orderItemId, List<MultipartFile> reviewImgFileList, Principal principal)
            throws Exception {
        Review review = ReviewMapper.convertToEntity(reviewFormDto);
        Member member = memberService.giveMember(memberService.checkEmail(principal));
        Long itemId = itemService.getItemFormDto(orderItemId);
        Item item = itemService.giveItem(itemId);
        review.setMember(member);
        review.setItem(item);
        reviewRepository.save(review);

        //이미지 등록
        for (int i = 0; i < reviewImgFileList.size(); i++) {
            ReviewImg reviewImg = new ReviewImg();
            reviewImg.setReview(review);
            reviewImgService.saveReview(reviewImg, reviewImgFileList);
        }
        itemService.changeReviewStatus(orderItemId);
    }

    public List<ReviewFormDto> giveReview(Long itemId) {
        List<Review> reviewList = reviewRepository.findByItemId(itemId);
        return ReviewMapper.convertToDtoList(reviewList);
    }

    public List<ReviewFormDto> giveMemberReview(Principal principal) {
        String email = memberService.checkEmail(principal);
        Member member = memberService.giveMember(email);
        List<Review> reviewList = reviewRepository.findByMemberId(member.getId());
        return ReviewMapper.convertToDtoList(reviewList);
    }

    public boolean existReviewCheck(Long itemId) {
        List<Review> reviewList = reviewRepository.findByItemId(itemId);
        return reviewList != null && !reviewList.isEmpty();
    }

    public List<ReviewImgDto> giveReviewImg(Long itemId) {
        List<Review> reviewList = reviewRepository.findByItemId(itemId);
        List<ReviewImg> reviewImgList = new ArrayList<>();
        for (Review review : reviewList) {
            List<ReviewImg> reviewImgs = reviewImgRepository.findByReviewId(review.getId());
            reviewImgList.add(reviewImgs.get(0));
        }
        return ReviewImgMapper.convertToDtoList(reviewImgList);
    }

    public List<ReviewImgDto> giveReviewImg(Principal principal) {
        String email = memberService.checkEmail(principal);
        Member member = memberService.giveMember(email);
        List<Review> reviewList = reviewRepository.findByMemberId(member.getId());
        List<ReviewImg> reviewImgList = new ArrayList<>();
        for (Review review : reviewList) {
            List<ReviewImg> reviewImgs = reviewImgRepository.findByReviewId(review.getId());
            if(!reviewImgs.isEmpty()){
                reviewImgList.add(reviewImgs.get(0));
            }
        }
        return ReviewImgMapper.convertToDtoList(reviewImgList);
    }
}

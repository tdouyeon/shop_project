package com.shop.service;

import com.shop.entity.ReviewImg;
import com.shop.repository.ReviewImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewImgService {

    @Value("${reviewImgLocation}") //application.properties에 itemImgLocation
    private String reviewImgLocation;

    private final ReviewImgRepository reviewImgRepository;
    private final FileService fileService;

    public void saveReview(ReviewImg reviewImg, List<MultipartFile> images) throws Exception {
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                String originalFilename = image.getOriginalFilename();
                System.out.println("파일 이름: " + originalFilename);
                String imgName = "";
                String imgUrl = "";
                if (!StringUtils.isEmpty(originalFilename)) { // oriImgName 문자열로 비어 있지 않으면 실행
                    System.out.println("******");
                    imgName = fileService.uploadFile(reviewImgLocation, originalFilename,
                            image.getBytes());
                    System.out.println(imgName);
                    imgUrl = "/images/review/" + imgName;
                }
                reviewImg.updateReviewImg(originalFilename, imgName, imgUrl);
                reviewImgRepository.save(reviewImg);
            }
        }
    }

    public List<ReviewImg> giveReviewImg(Long reviewId) {
        return reviewImgRepository.findByReviewId(reviewId);
    }
}

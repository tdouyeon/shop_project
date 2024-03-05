package com.shop.repository;

import com.shop.entity.Review;
import com.shop.entity.ReviewImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {

    @Query("SELECT ri FROM ReviewImg ri WHERE ri.review.id = :reviewId")
    List<ReviewImg> findByReviewId(@Param("reviewId") Long reviewId);

    List<ReviewImg> findByReview(Review review);

}

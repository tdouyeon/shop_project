package com.shop.repository;

import com.shop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.item.id = :itemId")
    List<Review> findByItemId(@Param("itemId") Long itemId);

    @Query("SELECT r FROM Review r WHERE r.member.id = :memberId")
    List<Review> findByMemberId(@Param("memberId") Long memberId);
}

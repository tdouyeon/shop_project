package com.shop.repository;

import com.shop.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndItemId(Long memberId, Long itemId);

    void deleteByMemberIdAndItemId(Long memberId, Long itemId);


}

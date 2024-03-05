package com.shop.repository;

import com.shop.entity.Item;
import com.shop.entity.Like;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberIdAndItemId(Long memberId, Long itemId);

    void deleteByMemberIdAndItemId(Long memberId, Long itemId);

    @Query("SELECT l.item FROM Like l " +
            "JOIN l.member m " +
            "WHERE m.email = :email")
    List<Item> findLikedItemsByEmail(@Param("email") String email);


    List<Like> findByMember(Member member);


}

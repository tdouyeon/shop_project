package com.shop.repository;

import com.shop.entity.Cart;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMemberId(Long memberId);

    List<Cart> findByMember(Member member);
}

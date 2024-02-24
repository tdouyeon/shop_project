package com.shop.repository;

import com.shop.constant.Role;
import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Member findByTel(String tel);

    Member findByEmailAndRole(String email, Role role);

}

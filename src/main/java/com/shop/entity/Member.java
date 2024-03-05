package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {
    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    private String address_detail;

    private String tel;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String chk;


    //Member 엔터티를 삭제할 때 관련된 Like 엔터티도 자동으로 삭제될 수 있도록 설정
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Like> likes;

    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder, String role) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        member.setAddress_detail(memberFormDto.getAddress_detail());
        member.setTel(memberFormDto.getTel());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);

        if (role.equals("admin")) {
            member.setRole(Role.ADMIN);
        } else {
            member.setRole(Role.USER);
        }
        return member;
    }
}
package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "admin")
@Getter
@Setter
@ToString
public class Admin extends BaseEntity{
    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    private String tel;
    @Enumerated(EnumType.STRING)
    private Role role;
    public static Admin createAdmin(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder){
        Admin admin = new Admin();
        admin.setName(memberFormDto.getName());
        admin.setEmail(memberFormDto.getEmail());
        admin.setAddress(memberFormDto.getAddress());
        admin.setTel(memberFormDto.getTel());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        admin.setPassword(password);
        admin.setRole(Role.ADMIN);
        return admin;
    }
}
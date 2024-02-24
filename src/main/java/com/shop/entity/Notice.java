package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.ItemFormDto;
import com.shop.dto.MemberFormDto;
import com.shop.dto.NoticeDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "notice")
@Getter
@Setter
public class Notice extends BaseEntity{
    @Id
    @Column(name = "notice_id")
    @GeneratedValue(strategy = GenerationType.AUTO) // 자동을 1씩 증가
    private Long id;
    private String title;
    private String content;

    private static ModelMapper modelMapper = new ModelMapper();

    public static Notice updateNotice(NoticeDto noticeDto) {
        return modelMapper.map(noticeDto, Notice.class);
    }
}

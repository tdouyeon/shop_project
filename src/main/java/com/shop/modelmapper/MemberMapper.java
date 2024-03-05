package com.shop.modelmapper;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static MemberFormDto convertToDto(Member member) {
        return modelMapper.map(member, MemberFormDto.class);
    }

    public static Member convertToEntity(MemberFormDto memberFormDto) {
        return modelMapper.map(memberFormDto, Member.class);
    }
}

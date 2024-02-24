package com.shop.modelmapper;

import com.shop.dto.NoticeDto;
import com.shop.dto.ReviewImgDto;
import com.shop.entity.Notice;
import com.shop.entity.ReviewImg;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NoticeMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static NoticeDto convertToDto(Notice notice) {
        return modelMapper.map(notice, NoticeDto.class);
    }

    public static Notice convertToEntity(NoticeDto noticeDto) {
        return modelMapper.map(noticeDto, Notice.class);
    }
}

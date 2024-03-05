package com.shop.modelmapper;

import com.shop.dto.NoticeDto;
import com.shop.entity.Notice;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class NoticeMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static NoticeDto convertToDto(Notice notice) {
        return modelMapper.map(notice, NoticeDto.class);
    }
}

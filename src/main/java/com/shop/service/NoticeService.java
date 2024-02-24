package com.shop.service;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.NoticeDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Notice;
import com.shop.modelmapper.NoticeMapper;
import com.shop.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service // 나 서비스다.
@Transactional // 트랜젝션설정 : 성공을하면 그대로 적용 실패하면 롤백
@RequiredArgsConstructor // final 또는 @NonNull 명령어가 붙으면 객체를 자동 붙혀줍니다.
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final MemberService memberService;

    public void saveNotice(NoticeDto noticeDto) {
        Notice notice = noticeDto.createNotice();
        noticeRepository.save(notice);
    }
    public int countNotice(Principal principal) {
        return noticeRepository.countByCreatedBy(memberService.checkEmail(principal)).intValue();
    }

    @Transactional(readOnly = true)
    public Page<Notice> getNotices(Pageable pageable) {
        return noticeRepository.getNotice(pageable);
    }

    public NoticeDto getNotice(Long noticeId) {
        Optional<Notice> findNotice = noticeRepository.findById(noticeId);

        return NoticeMapper.convertToDto(findNotice.get());
    }
}

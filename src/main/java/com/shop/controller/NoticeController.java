package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MemberFormDto;
import com.shop.dto.NoticeDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Notice;
import com.shop.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping(value = "/make")
    public String makeNotice(Model model) {
        model.addAttribute("noticeDto", new NoticeDto());
        return "notice/noticeForm";
    }

    @GetMapping(value = "/noticeDtl/{noticeId}")
    public String MngDtlNotice(@PathVariable("noticeId") Long noticeId, Model model) {
        NoticeDto notice = noticeService.getNotice(noticeId);
        model.addAttribute("noticeDto", notice);
        return "notice/noticeForm"; // 실제 페이지로의 이동
    }

    @PostMapping(value = "/make")
    public String makeNotice(@Valid NoticeDto noticeDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "notice/noticeForm";
        }
        try {
            noticeService.saveNotice(noticeDto);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "notice/noticeForm";
        }
        return "redirect:/adminMain"; // 다시 실행 /
    }


    @GetMapping(value = {"/notice", "/notice/{page}"})
    public String noticeView(@PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<Notice> notices = noticeService.getNotices(pageable);
        model.addAttribute("notices", notices);
        model.addAttribute("maxPage", 8);
        return "notice/notice";
    }

    @GetMapping(value = {"/Mng", "/Mng/{page}"})
    public String noticeMng(@PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<Notice> notices = noticeService.getNotices(pageable);
        model.addAttribute("notices", notices);
        model.addAttribute("maxPage", 8);
        return "notice/noticeMng";
    }
}

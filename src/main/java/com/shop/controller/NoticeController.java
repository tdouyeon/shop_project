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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    @GetMapping(value = "/make")
    public String memberForm(Model model){
        model.addAttribute("noticeDto",new NoticeDto());
        return "notice/noticeForm";
    }

    @PostMapping(value = "/make")
    public String memberForm(@Valid NoticeDto noticeDto,
                             BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }
        try {
            noticeService.saveNotice(noticeDto);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "notice/noticeForm";
        }
        return "redirect:/";
    }


    @GetMapping(value = {"/notice", "/notice/items/{page}"})
    public String noticeView(@PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        Page<Notice> notices = noticeService.getNotice(pageable);
        model.addAttribute("notices", notices);
        model.addAttribute("maxPage",8);
        return "notice/notice";
    }
}

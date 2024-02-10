package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MyPageController {

    @GetMapping(value = "/")
    public String ah(Model model){
        System.out.println("왔나?");
        model.addAttribute("noticeDto",new NoticeDto());
        return "notice/noticeForm";
    }
}

package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MyPageController {

    @GetMapping(value = "/mypage")
    public String ah(Model model){
        model.addAttribute("noticeDto",new NoticeDto());
        return "notice/noticeForm";
    }

    @PostMapping("/submit_review")
    @ResponseBody
    public String reviewMake(@PathVariable(value = "orderId") String orderId) {

        return "review/reviewMakeForm";
    }
}

package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.repository.ItemRepository;
import com.shop.service.ItemService;
import com.shop.service.OrderService;
import com.shop.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MyPageController {
    private final ItemService itemService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    @GetMapping(value = "/mypage")
    public String ah(Model model){
        model.addAttribute("noticeDto",new NoticeDto());
        return "notice/noticeForm";
    }



    @GetMapping("/submit_review")
    public String reviewMake(@RequestParam(name = "orderItemId") Long orderItemId, Model model) {
        Long itemId = itemService.getItemFormDto(orderItemId);
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("orderItemId", orderItemId);
        model.addAttribute("itemId", itemId);
        model.addAttribute("itemFormDto", itemFormDto);
        model.addAttribute("reviewFormDto", new ReviewFormDto());
        return "review/reviewMakeForm";
    }
    @PostMapping("/submit_review")
    public String reviewMake(@Valid ReviewFormDto reviewFormDto,  @RequestParam("orderItemId") Long orderItemId, @RequestParam("image") List<MultipartFile> images,
                             Principal principal, Model model) throws Exception {
        reviewService.saveReview(reviewFormDto, orderItemId, images, principal);
        Pageable pageable = PageRequest.of(0,5);

        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal, pageable);

        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);
        return "order/orderHist";
    }
}

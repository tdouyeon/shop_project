package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.service.CategoryService;
import com.shop.service.ItemService;
import com.shop.service.LikeService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final LikeService likeService;
    @GetMapping(value = "/")
    public String main() {
        categoryService.makeCategory();
        memberService.makeMember();
        return "main";
    }

    @GetMapping(value = "/shop")
    public String shopMain(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal ) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }
        if(principal != null) {
            Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable,"", principal);
            model.addAttribute("items", items);
        }
        else {
            Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable,"");
            model.addAttribute("items", items);
        }

        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",8);
        return "category/shop";
    }
    @GetMapping(value = "/shop/{categoryName}")
    @ResponseBody
    public ResponseEntity<Page<MainItemDto>> shopMain(ItemSearchDto itemSearchDto,
                                                      Optional<Integer> page,
                                                      @PathVariable(name = "categoryName", required = false) String categoryName,
                                                      Principal principal) {

        Pageable pageable = PageRequest.of(page.orElse(0), 8);
        if (itemSearchDto.getSearchQuery() == null) {
            itemSearchDto.setSearchQuery("");
        }

        Page<MainItemDto> items;
        if (categoryName != null) {
            String lowerCaseCategoryName = categoryName.toLowerCase();
            items = itemService.getMainItemPage(itemSearchDto, pageable, lowerCaseCategoryName,principal);
        } else {
            items = itemService.getMainItemPage(itemSearchDto, pageable, "", principal);
        }
        System.out.println("사이즈보자"+items.getContent().size());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @PostMapping(value = "/shop/liked")
    public ResponseEntity<Map<String, Object>> checkLiked(@RequestBody Map<String, Object> request, Principal principal) {

        if(principal == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        try {
            Long itemId = Long.parseLong(request.get("itemId").toString());
            boolean liked = Boolean.parseBoolean(request.get("liked").toString());
            // 가정: 로직 결과에 따라 liked 상태를 설정
            liked = likeService.checkLiked(itemId, liked, principal);

            // 응답 데이터 구성
            Map<String, Object> response = new HashMap<>();
            response.put("itemId", itemId);
            response.put("liked", liked);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

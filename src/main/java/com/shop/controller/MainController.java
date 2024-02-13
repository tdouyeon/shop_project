package com.shop.controller;

import com.shop.dto.CategoryDto;
import com.shop.dto.ItemAverageRatingDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.service.CategoryService;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {
        categoryService.makeCategory();
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        if(itemSearchDto.getSearchQuery() == null)
        {
            itemSearchDto.setSearchQuery("");
        }
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable,"");
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",8);
        return "main";
    }

    @GetMapping(value = "/shop")
    public String shopMain(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model ) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        if(itemSearchDto.getSearchQuery() == null) {
            itemSearchDto.setSearchQuery("");
        }
        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable, "");

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto",itemSearchDto);
        model.addAttribute("maxPage",8);
        return "category/shop";
    }
    @GetMapping(value = "/shop/{categoryName}")
    @ResponseBody
    public ResponseEntity<Page<MainItemDto>> shopMain(ItemSearchDto itemSearchDto,
                                                      Optional<Integer> page,
                                                      @PathVariable(name = "categoryName", required = false) String categoryName) {

        Pageable pageable = PageRequest.of(page.orElse(0), 8);
        if (itemSearchDto.getSearchQuery() == null) {
            itemSearchDto.setSearchQuery("");
        }

        Page<MainItemDto> items;
        if (categoryName != null) {
            String lowerCaseCategoryName = categoryName.toLowerCase();
            items = itemService.getMainItemPage(itemSearchDto, pageable, lowerCaseCategoryName);
        } else {
            items = itemService.getMainItemPage(itemSearchDto, pageable, "");
        }
        System.out.println("사이즈보자"+items.getContent().size());

        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}

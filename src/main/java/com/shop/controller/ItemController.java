package com.shop.controller;

import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.service.CategoryService;
import com.shop.service.ItemService;
import com.shop.service.MemberService;
import com.shop.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final MemberService memberService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategory();
        model.addAttribute("itemFormDto", new ItemFormDto());
        model.addAttribute("categoryDtos", categoryDtos);
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                          @RequestParam("itemDetailImgFile") List<MultipartFile> itemDetailFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage",
                    "대표 이미지를 1개 이상 등록해주세요.");
            return "item/itemForm";
        }
        if (itemDetailFileList.get(0).isEmpty()) {
            model.addAttribute("errorMessage",
                    "상품 상세 이미지를 1개 이상 등록해주세요.");
            return "item/itemForm";
        }
        try {
            System.out.println("이미지" + itemImgFileList.get(0).getBytes().toString());
            itemService.saveItem(itemFormDto, itemImgFileList, itemDetailFileList);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage",
                    "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/adminMain"; // 다시 실행 /
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategory();

        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
            // return "item/itemForm";
        }
        model.addAttribute("categoryDtos", categoryDtos);

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             @RequestParam("itemDetailImgFile") List<MultipartFile> itemDetailImgFile,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemImgFileList, itemDetailImgFile);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/adminMain"; // 다시 실행 /
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Principal principal,
                             Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 8);
        String email = memberService.checkEmail(principal);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable, email);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 8);
        return "item/itemMng";
    }

    @PostMapping(value = "/admin/getSubcategories")
    @ResponseBody
    List<CategoryDto> getSubcategories(@RequestParam("category") String category) {
        List<CategoryDto> categoryDtos = categoryService.findCategory(category);
        return categoryDtos;
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        List<ReviewFormDto> reviewFormDtos = new ArrayList<>();
        List<ReviewImgDto> reviewImgDtos = new ArrayList<>();
        if (reviewService.existReviewCheck(itemId)) {
            reviewFormDtos = reviewService.giveReview(itemId);
            reviewImgDtos = reviewService.giveReviewImg(itemId);
        }
        model.addAttribute("reviews", reviewFormDtos);
        model.addAttribute("reviewImgDtos", reviewImgDtos);
        model.addAttribute("item", itemFormDto);

        return "item/itemDtl";
    }
}

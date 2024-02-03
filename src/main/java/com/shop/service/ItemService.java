package com.shop.service;

import com.shop.dto.*;
import com.shop.entity.Item;
import com.shop.entity.ItemDetailImg;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemDetailImgRepository;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final ItemDetailImgService itemDetailImgService;
    private final ItemDetailImgRepository itemDetailImgRepository;
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, List<MultipartFile> itemDetailImgFileList)
            throws Exception{
        //상품등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);
        //이미지 등록
        for(int i =0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");
            itemImgService.saveItemImg(itemImg,itemImgFileList.get(i));
        }
        for(int i = 0; i < itemDetailImgFileList.size();i++){
            ItemDetailImg itemDetailImg = new ItemDetailImg();
            itemDetailImg.setItem(item);

            itemDetailImgService.saveItemDetailImg(itemDetailImg, itemDetailImgFileList.get(i));

        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        //Entity
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        //DB에서 데이터를 가지고 옵니다.
        List<ItemDetailImg> itemDetailImgList = itemDetailImgRepository.findByItemIdOrderByIdAsc(itemId);
        //DTO
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        List<ItemDetailImgDto> itemDetailImgDtoList = new ArrayList<>();

        for(ItemImg itemimg : itemImgList){
            // Entity -> DTO
            ItemImgDto itemImgDto = ItemImgDto.of(itemimg);
            itemImgDtoList.add(itemImgDto);
        }

        for(ItemDetailImg itemDetailImg : itemDetailImgList){
            ItemDetailImgDto itemDetailImgDto = ItemDetailImgDto.of(itemDetailImg);
            itemDetailImgDtoList.add(itemDetailImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        // Item -> ItemFormDto modelMapper
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        itemFormDto.setItemDetailImgDtoList(itemDetailImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList, List<MultipartFile> itemDetailImgFileList)
            throws Exception{
        //상품 변경
        Item item = itemRepository.findById(itemFormDto.getId()).
                orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        //상품 이미지 변경
        List<Long> itemImgIds = itemFormDto.getItemImgIds();
        List<Long> itemDetailImgIds = itemFormDto.getItemDetailImgIds();

        for(int i =0; i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        for(int i=0; i<itemDetailImgFileList.size();i++){
            itemDetailImgService.updateItemImg(itemDetailImgIds.get(i), itemDetailImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
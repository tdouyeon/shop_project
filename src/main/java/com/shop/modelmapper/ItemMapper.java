package com.shop.modelmapper;

import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static List<ItemDto> convertToDtoList(List<Item> itemList) {
        return itemList.stream()
                .map(item -> modelMapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }

    public static List<Item> convertToEntityList(List<ItemDto> itemDtoList) {
        return itemDtoList.stream()
                .map(itemDto -> modelMapper.map(itemDto, Item.class))
                .collect(Collectors.toList());
    }
}

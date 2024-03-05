package com.shop.service;

import com.shop.dto.ItemDto;
import com.shop.entity.Item;
import com.shop.entity.Like;
import com.shop.entity.Member;
import com.shop.modelmapper.ItemMapper;
import com.shop.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final ItemImgService itemImgService;

    public boolean checkLiked(Long itemId, boolean liked, Principal principal) {
        Member member = memberService.giveMember(memberService.checkEmail(principal));
        Long memberId = member.getId();
        if (liked) {
            Optional<Like> findLike = likeRepository.findByMemberIdAndItemId(memberId, itemId);
            if (findLike.isPresent()) {
                likeRepository.delete(findLike.get());
            }
            return false;
        } else {
            Item item = itemService.giveItem(itemId);
            likeRepository.save(Like.createLike(member, item));
            return true;
        }
    }

    public List<ItemDto> getLikedItemsByEmail(Principal principal) {
        String email = memberService.checkEmail(principal);
        List<ItemDto> itemDtoList = ItemMapper.convertToDtoList(likeRepository.findLikedItemsByEmail(email));
        List<String> imgUrlList = itemImgService.getImgUrl(itemDtoList);
        for (int i = 0; i < itemDtoList.size(); i++) {
            itemDtoList.get(i).setImgUrl(imgUrlList.get(i));
        }
        return itemDtoList;
    }
}

package com.shop.service;

import com.shop.entity.Item;
import com.shop.entity.Like;
import com.shop.entity.Member;
import com.shop.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    public boolean checkLiked(Long itemId, boolean liked, Principal principal) {
        Member member = memberService.giveMember(memberService.checkEmail(principal));
        Long memberId = member.getId();
        if (liked) {
            Optional<Like> findLike = likeRepository.findByMemberIdAndItemId(memberId, itemId);
            if (findLike.isPresent()) {
                likeRepository.deleteByMemberIdAndItemId(memberId, itemId);
            }
            return false;
        } else {
            Item item = itemService.giveItem(itemId);
            likeRepository.save(Like.createLike(member, item));
            return true;
        }
    }

}

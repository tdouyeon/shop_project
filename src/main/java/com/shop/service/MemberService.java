package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Map;

@Service // 나 서비스다.
@Transactional // 트랜젝션설정 : 성공을하면 그대로 적용 실패하면 롤백
@RequiredArgsConstructor // final 또는 @NonNull 명령어가 붙으면 객체를 자동 붙혀줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member); // 데이터베이스에 저장을 하라는 명령
    }
    public Member saveSnsMember(Member member) {
        return  memberRepository.save(member);
    }
    public boolean findMember(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null){
            return false;
        }
        return true;
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        if (!emailService.validCheck()){
            throw new IllegalStateException("이메일 인증은 필수입니다.");
        }
    }
    public String checkEmail(Principal principal) {
        String email="";
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            email = ((UsernamePasswordAuthenticationToken) principal).getName();
        }
        else{
            if(((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId().equals("google")) {
                OAuth2User oAuth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
                email = oAuth2User.getAttribute("email");
            }
            OAuth2User oAuth2User = ((OAuth2AuthenticationToken) principal).getPrincipal();
            Map<String, Object> attributes = oAuth2User.getAttributes();
            if(((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId().equals("naver")) {
                Map<String, Object> response2 = (Map<String, Object>) attributes.get("response");
                String realName = (String) response2.get("name");
                email = (String) response2.get("email");
            }
            if(((OAuth2AuthenticationToken) principal).getAuthorizedClientRegistrationId().equals("kakao")){
                Map<String, Object> profile = (Map<String, Object>) attributes.get("kakao_account");
                email = (String) profile.get("email");
            }
    }
        return email;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        //빌더패턴
        return User.builder().username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}

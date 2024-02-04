package com.shop.service;

import com.shop.dto.ChangePasswdFormDto;
import com.shop.dto.MemberFindFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import jakarta.validation.constraints.NotNull;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Service // 나 서비스다.
@Transactional // 트랜젝션설정 : 성공을하면 그대로 적용 실패하면 롤백
@RequiredArgsConstructor // final 또는 @NonNull 명령어가 붙으면 객체를 자동 붙혀줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final EmailService emailService;
    @NotNull
    private DefaultMessageService messageService;
    private static int number;  // 랜덤 인증 코드
    private static boolean checkValid;

    private final PasswordEncoder passwordEncoder;

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecret;

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
    };

    // throws javassist.NotFoundException: 상위 메소드로 예외 전파
    public String findEmail(MemberFindFormDto findFormDto) throws javassist.NotFoundException {
        Member member = memberRepository.findByTel(findFormDto.getTel());
        if (member != null) {
            if (!member.getName().equals(findFormDto.getName())) {
                throw new NotFoundException("입력하신 정보와 일치하는 회원이 없습니다.");
            }

        } else {
            throw new NotFoundException("입력하신 정보와 일치하는 회원이 없습니다.");
        }
        return member.getEmail();
    }

    public Member checkUser(MemberFindFormDto findFormDto) throws javassist.NotFoundException {
        Member member;
        if(findFormDto.getWay().equals("EMAIL")){
            member = memberRepository.findByEmail(findFormDto.getEmail());
        } else {
            member = memberRepository.findByTel(findFormDto.getTel());
        }
        if (member != null) {
            if (!member.getName().equals(findFormDto.getName())) {
                throw new NotFoundException("입력하신 정보와 일치하는 회원이 없습니다.");
            }

        } else {
            throw new NotFoundException("입력하신 정보와 일치하는 회원이 없습니다.");
        }
        return member;
    };


    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        if (!emailService.validCheck()){
            throw new IllegalStateException("이메일 인증은 필수입니다.");
        }
        if (!checkValid) {
            throw new IllegalStateException("전화번호 인증은 필수입니다.");
        }
    }
    public void changePasswd(ChangePasswdFormDto changePasswdFormDto) {
        Member member = memberRepository.findByEmail(changePasswdFormDto.getEmail());
        String hashedPassword = passwordEncoder.encode(changePasswdFormDto.getPassword());
        member.setPassword(hashedPassword);
        memberRepository.save(member);
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

    private static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public String sendMmsByResourcePath(String to) throws IOException {
        messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
        createNumber();
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01092715401");
        message.setTo(to);
        message.setText("[CRANK] 인증번호 ["+number+"] 입니다.");
        System.out.println("인증번호"+number);
        // 여러 건 메시지 발송일 경우 send many 예제와 동일하게 구성하여 발송할 수 있습니다.
        // this.messageService.sendOne(new SingleMessageSendingRequest(message));

        return number+"";
    }

    public boolean checkNumber(String num){
        int checkNumber = Integer.parseInt(num);
        if(checkNumber==number){
            checkValid = true;
            return true;
        }
        return false;
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

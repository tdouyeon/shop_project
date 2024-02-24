package com.shop.controller;

import com.shop.dto.ChangePasswdFormDto;
import com.shop.dto.MemberFindFormDto;
import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.EmailService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;

    @GetMapping(value = "/new/{userType}")
    public String memberForm(@PathVariable String userType, Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        if(userType.equals("admin")) {
            return "member/memberAdminForm";
        }
        return "member/memberForm";
    }

    @GetMapping(value = "/sendSMS")
    @ResponseBody
    public String sendSMS(String to) throws IOException {
        return memberService.sendMmsByResourcePath(to);
    }

    @PostMapping("/{num}/confirmNum")
    @ResponseBody
    ResponseEntity confirmNum(@PathVariable("num") String num) throws Exception {
        if (memberService.checkNumber(num)) {
            return new ResponseEntity<String>("인증이 완료되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증번호가 잘못 입력되었습니다.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/new/{userType}")
    public String memberForm(@PathVariable String userType, @Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            if(userType.equals("admin")){
                return "member/memberAdminForm";
            }
            return "member/memberForm";
        }
        try {
            memberService.saveMember(memberFormDto, userType);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login/{userType}")
    public String loginMember(@PathVariable String userType, Model model) {
        model.addAttribute("userType", userType);
        return "member/memberLoginForm";
    }

    @PostMapping("/{email}/mailConfirm")
    @ResponseBody
    String mailConfirm(@PathVariable("email") String email) throws Exception {
        return emailService.sendEmail(email);
    }

    @PostMapping("/{number}/confirmNumber")
    @ResponseBody
    ResponseEntity confirmNumber(@PathVariable("number") String number) throws Exception {
        if (emailService.checkNumber(number)) {
            return new ResponseEntity<String>("인증이 완료되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증번호가 잘못 입력되었습니다.", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/login/error/{userType}")
    public String handleLoginError(@PathVariable String userType, Model model) {
        model.addAttribute("userType", userType);
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/memberLoginForm";
    }

    @GetMapping(value = "/findId")
    public String findId(Model model) {
        model.addAttribute("memberFindFormDto", new MemberFindFormDto());
        return "member/findIdForm";
    }

    @PostMapping(value = "/findId")
    public String findId(@Valid MemberFindFormDto memberFindFormDto, Model model) {
        try {
            String email = memberService.findEmail(memberFindFormDto);
            model.addAttribute("email", email);
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/findIdForm";
        }
        return "member/emailView";
    }

    @GetMapping(value = "/findPasswd")
    public String findPasswd(Model model) {
        model.addAttribute("memberFindFormDto", new MemberFindFormDto());
        return "member/findPasswdForm";
    }

    @PostMapping(value = "/findPasswd")
    public String findPasswd(@Valid MemberFindFormDto memberFindFormDto, Model model) {
        try {
            Member member = memberService.checkUser(memberFindFormDto);
            ChangePasswdFormDto changePasswdFormDto = new ChangePasswdFormDto();
            changePasswdFormDto.setEmail(member.getEmail());
            model.addAttribute("changePasswdFormDto", changePasswdFormDto);
        } catch (NotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/findPasswdForm";
        }

        return "member/changePasswdForm";
    }

    @PostMapping(value = "/changePasswd")
    ResponseEntity<String> changePasswd(@Valid ChangePasswdFormDto changePasswdFormDto) {
        try {
            memberService.changePasswd(changePasswdFormDto);
        } catch (Exception e) {
            return new ResponseEntity<String>("비밀번호 변경 중 에러가 발생 했습니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("비밀번호 변경이 완료 되었습니다.", HttpStatus.OK);
    }

    @GetMapping(value = "inf")
    @ResponseBody
    public Member giveMemberinf(Principal principal) {
        return memberService.giveMember(memberService.checkEmail(principal));
    }
}

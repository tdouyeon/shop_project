package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.EmailService;
import com.shop.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, HttpServletRequest request, Model model){
        String [] arr = request.getParameterValues("chk");
        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }
        if(!arr[0].equals("on") || !arr[1].equals("on")){
            model.addAttribute("errorMessage","이용약관에 동의해주세요.");
            return "member/memberForm";
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            String chk = String.join(", ", arr);
            member.setChk(chk);
            memberService.saveMember(member);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "member/memberForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/adminNew")
    public String adminForm(Model model){
        model.addAttribute("memberFormDto",new MemberFormDto());
        return "admins/adminForm";
    }
    @PostMapping(value = "/adminNew")
    public String adminForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult,HttpServletRequest request, Model model){
        String [] arr = request.getParameterValues("chk");
        if(bindingResult.hasErrors()){
            return "admins/adminForm";
        }
        if(!arr[0].equals("on") || !arr[1].equals("on")){
            model.addAttribute("errorMessage","이용약관에 동의해주세요.");
            return "admins/adminForm";
        }
        try {
            Member member = Member.createAdmin(memberFormDto, passwordEncoder);
            String chk = String.join(", ", arr);
            member.setChk(chk);
            memberService.saveMember(member);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            return "admins/adminForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/adminLogin")
    public String loginAdmin(){
        return "/member/memberLoginForm";
    }
    @GetMapping(value = "/adminLogin/error")
    public String loginAdminError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }
    @PostMapping("/{email}/mailConfirm")
    @ResponseBody
    String mailConfirm(@PathVariable("email") String email) throws Exception {
        String num = emailService.sendEmail(email) + "";
        return num;
    }

    @PostMapping("/{number}/confirmNumber")
    @ResponseBody
    ResponseEntity confirmNumber(@PathVariable("number") String number) throws Exception {
        if(emailService.checkNumber(number)){
            return new ResponseEntity<String>("인증이 완료되었습니다.", HttpStatus.OK);
        }
        return new ResponseEntity<String>("인증번호가 잘못 입력되었습니다.", HttpStatus.BAD_REQUEST);
    }
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }
}

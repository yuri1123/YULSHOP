package com.yuri.shoppingsite.controller;

import com.yuri.shoppingsite.Repository.MemberRepository;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberFormDto;
import com.yuri.shoppingsite.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //회원가입 페이지로 이동
    @GetMapping("/signup")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/signup";
    }

    //회원가입
    @PostMapping("/signup")
    public String signup(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                         Model model){

        if(bindingResult.hasErrors()){
            return "member/signup";
        }

        try {
            Member member = Member.createMember(memberFormDto,passwordEncoder);
            memberService.saveMember(member);
        } catch(IllegalStateException e){
            model.addAttribute("errorMessage",e.getMessage());
            System.out.println(e.getMessage());
            return "member/signup";
        }

        return "redirect:/";
    }

    //    로그인페이지로 이동
    @GetMapping("/login")
    public String gologin(){
        return "member/login";
    }

    // 로그인 페이지 오류 메시지 전달
    @GetMapping("/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "member/login";
    }

    MemberRepository memberRepository;
    Member member;
    //마이페이지로 이동
    @GetMapping("/mypage")
    public String mypage(Model model,Principal principal){
        String nickname = memberService.getNickname(principal.getName());
        Role member = memberService.getAuth(principal.getName());
        model.addAttribute("member",member);
        model.addAttribute("nickname",nickname);
        return "member/mypage";
    }

    //회원정보 페이지로 이동
    @GetMapping("/personalinfo")
    public String personalInfo(Model model, Principal principal){
        MemberFormDto memberFormDto = memberService.getmember(principal.getName());
        model.addAttribute("memberFormDto", memberFormDto);
        return "member/personalinfo";
    }

    //회원정보 수정하기
    @PostMapping("/update")
    public String memberUpdate(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Principal principal, PasswordEncoder passwordEncoder,
                             Model model){

        if(bindingResult.hasErrors()){
            return "member/personalinfo";
        }
        try{
           memberService.updateMember(memberFormDto, principal, passwordEncoder);
        } catch (Exception e){
            model.addAttribute("errorMessage", "회원 정보 수정 중 에러가 발생하였습니다.");
            return "member/personalinfo";
        }
        return "member/mypage";
    }

    //쿠폰 관리 페이지로 이동
    @GetMapping("/coupon")
    public String coupon(){
        return "member/coupon";
    }

    //내가 쓴 게시물 보기 페이지로 이동
    @GetMapping("/myboard")
    public String myboard(){
        return "member/myboard";
    }


    //배송지관리 리스트페이지로 이동
    @GetMapping("/delivery/list")
    public String deleverylist(){
        return "member/deliveryaddresslist";
    }

    //배송지관리 등록페이지로 이동
    @GetMapping("/delivery/enroll")
    public String deliveryenroll(){
        return "member/deliveryaddressenroll";
    }



}

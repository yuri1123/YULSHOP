package com.yuri.shoppingsite.controller;

import com.yuri.shoppingsite.Repository.MemberRepository;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberAccountDto;
import com.yuri.shoppingsite.domain.user.MemberFormDto;
import com.yuri.shoppingsite.service.CompanyService;
import com.yuri.shoppingsite.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final CompanyService companyService;

    //회원가입 페이지로 이동
    @GetMapping("/signup")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/signup";
    }

    //회원가입
    @PostMapping("/signup")
    public String signup(@Valid MemberFormDto memberFormDto,
                         BindingResult bindingResult,
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
    public String gologin(Model model){
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
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
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/mypage";
    }

    //회원정보 페이지로 이동
    @GetMapping("/personalinfo")
    public String personalInfo(Model model, Principal principal){
        MemberFormDto memberFormDto = memberService.getmemberDto(principal.getName());
        MemberAccountDto memberAccountDto = memberService.getAccountDto(principal.getName());
        model.addAttribute("memberFormDto", memberFormDto);
        model.addAttribute("memberAccountDto", memberAccountDto);
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/personalinfo";
    }

    //회원정보 수정하기
    @PostMapping("/update")
    public String memberUpdate(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Principal principal,
                             Model model) throws Exception {
        System.out.println("컨트롤러에 옴");
        Long result = memberService.updateMember(memberFormDto, principal);
        System.out.println(result);
//        if(bindingResult.hasErrors()){
//            return "member/personalinfo";
//        }
//        try{
//           memberService.updateMember(memberFormDto, principal);
//        } catch (Exception e){
//            model.addAttribute("errorMessage", "회원 정보 수정 중 에러가 발생하였습니다.");
//            return "member/personalinfo";
//        }
        return "redirect:/member/personalinfo";
    }

    //회원정보 수정하기 - 환불계좌 등록하기
    @PostMapping("/update/account")
    public String updateAccount(@Valid MemberAccountDto memberAccountDto,
                                BindingResult bindingResult, Principal principal,
                                Model model) throws Exception {
        System.out.println("환불계좌 등록하기");
        Long result = memberService.updateAccount(memberAccountDto, principal);
        System.out.println(result);
        return "redirect:/member/personalinfo";
    }

    //쿠폰 관리 페이지로 이동
    @GetMapping("/newpassword")
    public String coupon(Principal principal, Model model){
        MemberFormDto memberFormDto = memberService.getmemberDto(principal.getName());
        model.addAttribute("memberFormDto", memberFormDto);
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/newpassword";
    }

    //내가 쓴 게시물 보기 페이지로 이동
    @GetMapping("/myboard")
    public String myboard(Model model){
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/myboard";
    }


    //배송지관리 리스트페이지로 이동
    @GetMapping("/delivery/list")
    public String deleverylist(Model model){
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/deliveryaddresslist";
    }

    //배송지관리 등록페이지로 이동
    @GetMapping("/delivery/enroll")
    public String deliveryenroll(Model model){
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/deliveryaddressenroll";
    }



}

package com.yuri.shoppingsite.controller;

import com.yuri.shoppingsite.Repository.MemberRepository;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.auth.PrincipalDetails;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.CommunitySearchDto;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberAccountDto;
import com.yuri.shoppingsite.domain.user.MemberFormDto;
import com.yuri.shoppingsite.domain.user.MemberSearchDto;
import com.yuri.shoppingsite.service.BoardService;
import com.yuri.shoppingsite.service.CompanyService;
import com.yuri.shoppingsite.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    @Autowired
    private final CompanyService companyService;
    private final BoardService boardService;

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

        return "redirect:/member/login";
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

    //마이페이지로 이동
    @GetMapping("/mypage")
    public String mypage(Model model,Principal principal){
        String name = principal.getName();
        Role member = memberService.getAuth(principal.getName());
        model.addAttribute("member",member);
        model.addAttribute("name",name);
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

    //새비번 페이지로 이동
    @GetMapping("/newpassword")
    public String newpassword(Principal principal, Model model){
        MemberFormDto memberFormDto = memberService.getmemberDto(principal.getName());
        model.addAttribute("memberFormDto", memberFormDto);
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);
        model.addAttribute("company",company);
        return "member/newpassword";
    }

    //비번 변경
    @PostMapping("/newpassword")
    public String newpassword(Principal principal,
                              @RequestParam("currentPassword") String currentPassword,
                              @RequestParam("newPassword") String newPassword,
                              RedirectAttributes redirectAttributes){
        String username = principal.getName();
        Member member = memberRepository.findByName(username);

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "기존 비밀번호가 일치하지 않습니다.");
            return "redirect:/member/newpassword";
        }

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        SecurityContextHolder.clearContext();

        redirectAttributes.addFlashAttribute("success", "비밀번호가 변경되었습니다. 다시 로그인해주세요.");
        return "redirect:/member/mypage";
    }


    //내가 쓴 게시물 보기 페이지로 이동
    @GetMapping(value = {"/myboard","/myboard/{page}"})
    public String myboard(Model model, Principal principal, CommunitySearchDto communitySearchDto,
                          @PathVariable("page") Optional<Integer> page){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Board> boardList = boardService.getMyBoardList(communitySearchDto, pageable, principal.getName());
        model.addAttribute("boardList", boardList);
        model.addAttribute("communitySearchDto", communitySearchDto);
        model.addAttribute("maxPage", 10);

        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        model.addAttribute("company",company);
        return "member/myboard";
    }

}

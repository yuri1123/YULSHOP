//package com.yuri.shoppingsite.controller;
//
//import com.yuri.shoppingsite.domain.user.UserCreateForm;
//import com.yuri.shoppingsite.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import javax.validation.Valid;
//
//@RequiredArgsConstructor
//@Controller
//public class UserController {
//
//    @Autowired
//    private final UserService userService;
//
//    //로그인페이지로 이동
//    @GetMapping("member/login")
//    public void gologin(){
//    }
////
////    @PostMapping("member/login")
////    public String login(Model model){
//////    model.addAttribute("login",)
////        return "redirect:/";
////    }
//
//    //회원가입페이지로 이동
//    @GetMapping("member/signup")
//    public String gosignup(UserCreateForm userCreateForm){
//        return "member/signup";
//    }
//
////    //회원가입하기
////    @PostMapping("member/signup")
////    public String signup(MemberDTO memberDto){
////        memberService.register(memberDto);
////        return "member/login";
////    }
//
//    //회원가입 하기
//    @PostMapping("member/signup")
//    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult){
//        if(bindingResult.hasErrors()){
//            return "member/signup";
//        }
//
//        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){
//            bindingResult.rejectValue("password2",
//                    "passwordInCorrect",
//                    "2개의 패스워드가 일치하지 않습니다.");
//            return "member/signup";
//        }
//        userService.create(
//                userCreateForm.getUsername(),
//                userCreateForm.getPassword1(),
//                userCreateForm.getEmail(),
//                userCreateForm.getNickname(),
//                userCreateForm.getBirth(),
//                userCreateForm.getPhone());
//
//        return "redirect:/";
//    }
//
////    @GetMapping("/login")
////    public String login(){
////        return "member/login";
////    }
//
//}

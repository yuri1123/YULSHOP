//package com.yuri.shoppingsite.controller;
//
//import com.yuri.shoppingsite.Repository.MemberRepository;
//import com.yuri.shoppingsite.domain.user.Member;
//import com.yuri.shoppingsite.domain.user.MemberFormDto;
//import com.yuri.shoppingsite.service.MemberService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
//import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.TestPropertySources;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.transaction.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//@AutoConfigureMockMvc //MockMvc 테스트를 위해 선언
//class MemberControllerTest {
//
//    @Autowired
//    MemberService memberService;
//
//    //실제 객체와 비슷하지만 테스트에 필요한 기능만 가지는 가짜 객체임
//    //MockMvc객체를 이용하면 웹 브라우저에서 요청을 하는 것처럼 테스트 할 수 있음
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    public Member createMember(String name, String password){
//        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setEmail(name);
//        memberFormDto.setEmail("yuri@gmail.com");
//        memberFormDto.setAddress("율율이네");
//        memberFormDto.setPassword(password);
//        Member member = Member.createMember(memberFormDto, passwordEncoder);
//        return memberService.saveMember(member);
//    }
//
////    @Test
////    @DisplayName("로그인 테스트")
////    public void login() throws Exception(){
////        String name = "admin12345";
////        String password = "1234";
////        this.createMember(name,password);
////        mockMvc.perform(formLogin().userParameter("name")
////                .loginProcessingUrl("/member/login")
////                .user(name).password(password))
////                .andExpect(SecurityMockMvcResultMatchers.authenticated());
////    }
//
//
//}
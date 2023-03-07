//package com.yuri.shoppingsite.service;
//
//import com.yuri.shoppingsite.Repository.MemberRepository;
//import com.yuri.shoppingsite.domain.user.Member;
//import com.yuri.shoppingsite.domain.user.MemberFormDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//
//import javax.transaction.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional //테스트 실행 후 롤백 처리가 된다. 메소드 반복 테스트가 가능
//@TestPropertySource(locations = "classpath:application-test.properties")
//class MemberServiceTest {
//
//    @Autowired
//    MemberService memberService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    public Member createMember(){ //member 엔티티 만드는 메소드 작성
//        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setEmail("test@email.com");
//        memberFormDto.setName("홍길동");
//        memberFormDto.setAddress("서울시 길동구");
//        memberFormDto.setNickname("율류리룽구링");
//        memberFormDto.setPassword("1234");
//        memberFormDto.setBirth("2022-10-30");
//        memberFormDto.setPhone("010-1234-5678");
//        return Member.createMember(memberFormDto, passwordEncoder);
//    }
//
//   @Test
//   @DisplayName("회원가입 테스트")
//    public void saveMemberTest(){ //assertEquals 메소드를 이용하여 저장하려고 요청했던 값과 실제 저장된 값 비교
//        Member member = createMember();
//        Member savedMember = memberService.saveMember(member);
//
//        //assertEquals 메소드 이용, 저장하려고 요청했던 값과 실제 저장 데이터 비교
//        assertEquals(member.getEmail(),savedMember.getEmail());
//        //첫번째 파라미터에는 기대 값, 두번째 파라미터에는 실제로 저장된 값을 넣어준다.
//        assertEquals(member.getName(), savedMember.getName());
//        assertEquals(member.getAddress(), savedMember.getAddress());
//        assertEquals(member.getNickname(), savedMember.getNickname());
//        assertEquals(member.getPassword(), savedMember.getPassword());
//        assertEquals(member.getBirth(), savedMember.getBirth());
//        assertEquals(member.getPhone(), savedMember.getPhone());
//        assertEquals(member.getRole(), savedMember.getRole());
//   }
//
//    @Test
//    @DisplayName("중복 회원 가입 테스트")
//    public void saveDuplicateMemberTest(){
//        Member member1 = createMember();
//        Member member2 = createMember();
//        memberService.saveMember(member1);
//
//        //assertThrows 메소드 이용하면 예외 처리 테스트 가능
//        //첫번째 파라미터에는 발생할 예외 타입 넣어줌
//        Throwable e = assertThrows(IllegalStateException.class, ()->{
//            memberService.saveMember(member2);});
//        //발생할 예외 메시지가 예상 결과와 맞는지 검증
//        assertEquals("이미 가입된 회원입니다",e.getMessage());
//    }
//
//}
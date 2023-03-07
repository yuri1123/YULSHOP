//package com.yuri.shoppingsite.domain.userFunction;
//
//import com.yuri.shoppingsite.Repository.CartRepository;
//import com.yuri.shoppingsite.Repository.MemberRepository;
//import com.yuri.shoppingsite.domain.shop.Cart;
//import com.yuri.shoppingsite.domain.user.Member;
//import com.yuri.shoppingsite.domain.user.MemberFormDto;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityNotFoundException;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//class CartTest {
//
//    @Autowired
//    CartRepository cartRepository;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @PersistenceContext
//    EntityManager em;
//
//    //회원 엔티티 생성하는 메소드
////    public Member createMember(){
////        MemberFormDto memberFormDto = new MemberFormDto();
////        memberFormDto.setName("user1");
////        memberFormDto.setPassword("1234");
////        memberFormDto.setEmail("user@gmail.com");
////        memberFormDto.setPostcode(|);
////        memberFormDto.setAddress("유저시 유저동");
////        memberFormDto.setNickname("유저1");
////        memberFormDto.setBirth("2010-01-15");
////        memberFormDto.setPhone("010-1234-5678");
////        return Member.createMember(memberFormDto, passwordEncoder);
////    }
//
//    @Test
//    @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
//    public void findCartAndMemberTest(){
//        Member member = createMember();
//        memberRepository.save(member);
//
//        Cart cart = new Cart();
//        cart.setMember(member);
//        cartRepository.save(cart);
//
//        //jpa는 영속성 컨텍스트에 데이터 저장 후 트랜잭션이 끝날 때 flush()를 호출하여 데이터베이스에 반영한다.
//        //회원 엔티티와 장바구니 엔티티를 영속성 컨텍스트에 저장 후 엔티티 매니저로부터 강제로 flush()를 호출하여 데이터베이스에 반영
//        em.flush();
//        //JPA는 영속성 컨텍스트로부터 엔티티 조회후 영속성 컨텍스트에 엔티티가 없을 경우 데이터베이스를 조회한다.
//        //실제 데이터베이스에서 장바구니 엔티티를 가지고 올때 회원 엔티티도 같이 가져오는지 보기 위해 영속성 컨텍스트를 비워주겠다.
//        em.clear();
//        //지정된 장바구니 엔티티를 조회한다.
//        Cart savedCart = cartRepository.findById(cart.getId())
//                .orElseThrow(EntityNotFoundException::new);
//        //처음에 저장한 member엔티티의 id와 savedCart에 매핑된 member엔티티의 id를 비교한다.
//        assertEquals(savedCart.getMember().getId(), member.getId());
//
//
//
//    }
//
//
//
//}
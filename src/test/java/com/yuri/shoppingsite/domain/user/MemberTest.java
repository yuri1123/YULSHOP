package com.yuri.shoppingsite.domain.user;

import com.yuri.shoppingsite.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    //스프링 시큐리티에서 제공하는 어노테이션으로 @WithMockUser에 지정한 사용자가 로그인한 상태라고 가정하고 테스트진행
    @WithMockUser(username = "yuri123", roles="USER")
    public void auditing2Test(){
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("register time:" + member.getRegTime());
        System.out.println("update time:" +member.getUpdateTime());
        System.out.println("create member:"+ member.getCreatedBy());
        System.out.println("modify member:"+ member.getModifiedBy());
    }
}
package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.MemberRepository;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.shop.*;
import com.yuri.shoppingsite.domain.user.MemberFormDto;
import com.yuri.shoppingsite.domain.user.MemberSearchDto;
import com.yuri.shoppingsite.domain.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional //비즈니스 로직 담당 서비스 계층 클래스에 선언, 로직 처리시 에러가 발생하면
//변경된 데이터 로직을 수행하기 이전 상태로 콜백 시켜준다.
@RequiredArgsConstructor //final이나 @NonNull이 붙은 필드에 생성자 생성
public class MemberService implements UserDetailsService {

    //멤버의 상태를 저장함(중복되지 않는)
    @Autowired
    private final MemberRepository memberRepository;
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    //이미 가입된 회원은 IllegalStateException 예외 발생
    public void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        Member findName = memberRepository.findByName(member.getName());
        if(findMember != null || findName != null){
            throw new IllegalStateException("이미 가입된 회원입니다");
        }
    }

    @Override
    //로그인할 유저의 아이디(이름)를 가져옴
    public UserDetails loadUserByUsername(String name)
            throws UsernameNotFoundException {

        Member member = memberRepository.findByName(name);

        if(member == null){
            throw new UsernameNotFoundException(name);
        }

        //UserDetail을 구현하고 있는 User 객체를 반환한다.
        //User 객체를 생성하기 위해 생성자로 회원의 이메일, 비번, role을 파라미터로 넘겨준다.
        return User.builder()
                .username(member.getName())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

    }
    //현재 로그인한 회원의 정보 가져오기
    @Transactional(readOnly = true)
    public MemberFormDto getmember(String name){
        Member member = memberRepository.findByName(name);
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        return memberFormDto;
    }
    //현재 로그인한 회원의 권한 가져오기
    @Transactional(readOnly = true)
    public boolean validateMember(String name){
        Role member = memberRepository.findByName(name).getRole();
        //현재 로그인한 회원이 ADMIN인지 admin이면 true, 아니면 false 반환
        if(!StringUtils.equals(member,
                com.yuri.shoppingsite.constant.Role.ADMIN)){
            return false;
        }
        return true;
    }

    //현재 로그인한 회원의 권한가져오기
    @Transactional(readOnly = true)
    public Role getAuth(String name){
        Role member = memberRepository.findByName(name).getRole();
    return member;
    }

    //회원의 닉네임 가져오기
    public String getNickname(String name){
        String nickname = memberRepository.findByName(name).getNickname();
        return nickname;
    }

    public Page<Member> getMemberAuth(MemberSearchDto memberSearchDto,Pageable pageable){
        return memberRepository.getAdminMemberPage(memberSearchDto, pageable);
    }

    //멤버 정보 수정하기
    public Long updateMember(MemberFormDto memberFormDto, Principal principal, PasswordEncoder passwordEncoder) throws Exception{

        Member member = memberRepository.findByName(principal.getName());
        member.updateMember(memberFormDto, passwordEncoder);
        return member.getId();
    }

    public void deleteMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        memberRepository.delete(member);
    }

//    public void updateMemberRole(Long id){
//        Member member = memberRepository.findById(id)
//                .orElseThrow(EntityNotFoundException::new);
//        memberRepository.
//    }

    //회원수 가져오기
    public int getMemberCount(){
        int memberCount = (int) memberRepository.count();
        return memberCount;
    }

    //멤버 권한 업데이트
    public int updateUserRole(Long id, Role role){
        int result = memberRepository.updateUserRole(id,role);
        return result;
    }

}


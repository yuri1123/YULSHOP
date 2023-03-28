package com.yuri.shoppingsite.domain.user;

import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.common.BaseEntity;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name="member")
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(unique = true)
    private String email; //이메일로 구분되어야 하므로 동일한 값 못오도록 unique 속성 지정
    private String password;
    private String postcode;
    private String address1;
    private String address2;
    private String nickname;
    private String birth;
    private String phone;
    private String smsreceive;
    private String emailreceive;
    private String accountbank;
    private String accountnum;
    private String accountname;

    @Enumerated(EnumType.STRING) //자바의 이넘타입을 엔티티 속성으로 지정
    //enum을 사용할 때 기본적으로 순서 저장되는데, enum의 순서가 바뀔 경우 문제가 발생할 수
    //있기 때문에 "EnumType.STRING" 옵션을 사용해서 String으로 지정하기를 권장
    private Role role;

    private String provider;    // oauth2를 이용할 경우 어떤 플랫폼을 이용하는지
    private String providerId;  // oauth2를 이용할 경우 아이디값


    //Member 엔티티를 생성하는 메소드. 회원생성 메소드를 엔티티에 넣으면
    // 코드 변경시 한군데만 수정하면 되는 이점이 있음
    public static Member createMember(MemberFormDto memberFormDto,
                                      PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setPostcode(memberFormDto.getPostcode());
        member.setAddress1(memberFormDto.getAddress1());
        member.setAddress2(memberFormDto.getAddress2());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setNickname(memberFormDto.getNickname());
        member.setBirth(memberFormDto.getBirth());
        member.setPhone(memberFormDto.getPhone());
        member.setRole(Role.USER);
        member.setSmsreceive(memberFormDto.getSmsreceive());
        member.setEmailreceive(memberFormDto.getEmailreceive());
        return member;
    }

    public void updateMember(MemberFormDto memberFormDto) {
        this.name = memberFormDto.getName();
        this.email = memberFormDto.getEmail();
        this.postcode = memberFormDto.getPostcode();
        this.address1 = memberFormDto.getAddress1();
        this.address2 = memberFormDto.getAddress2();
        this.nickname = memberFormDto.getNickname();
        this.birth = memberFormDto.getBirth();
        this.phone = memberFormDto.getPhone();
        this.smsreceive = memberFormDto.getSmsreceive();
        this.emailreceive = memberFormDto.getEmailreceive();
    }

    public void updateAccount(MemberAccountDto memberAccountDto){
        this.name = memberAccountDto.getName();
        this.accountbank = memberAccountDto.getAccountbank();
        this.accountnum = memberAccountDto.getAccountnum();
        this.accountname = memberAccountDto.getAccountname();
    }

    @Builder(builderClassName = "UserDetailRegister", builderMethodName = "userDetailRegister")
    public Member(String name, String password, String email, Role role) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    @Builder(builderClassName = "OAuth2Register", builderMethodName = "oauth2Register")
    public Member(String name, String password, String email, Role role, String provider, String providerId) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
    public Member(String name, String password, String nickname, String birth, String phone, List<GrantedAuthority> authorities) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

}
package com.yuri.shoppingsite.domain.user;

import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Getter
@Setter
public class MemberAccountDto {

    private String name;
    private String accountbank;
    private String accountnum;
    private String accountname;

    private static ModelMapper modelMapper = new ModelMapper();
    public static  MemberAccountDto of(Member member){

        return modelMapper.map(member, MemberAccountDto.class);
    }



}
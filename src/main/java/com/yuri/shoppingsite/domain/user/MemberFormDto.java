package com.yuri.shoppingsite.domain.user;

import com.yuri.shoppingsite.domain.shop.Item;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class MemberFormDto {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String name;
    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;
    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(max=16, message = "비밀번호는 16자리 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String postcode;
    private String address1;
    @NotEmpty(message = "상세주소는 필수 입력 값입니다.")
    private String address2;
    private String nickname;
    private String birth;
    private String phone;
    @NotEmpty(message = "SMS수신여부는 필수 입력 값입니다.")
    private String smsreceive;
    @NotEmpty(message = "EMAIL수신여부는 필수 입력 값입니다.")
    private String emailreceive;


    private static ModelMapper modelMapper = new ModelMapper();

    public static  MemberFormDto of(Member member){

        return modelMapper.map(member, MemberFormDto.class);
    }

}

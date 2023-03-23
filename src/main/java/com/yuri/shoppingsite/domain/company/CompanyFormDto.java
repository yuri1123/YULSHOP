package com.yuri.shoppingsite.domain.company;

import com.yuri.shoppingsite.domain.user.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Setter
@Getter
public class CompanyFormDto {

    @NotBlank(message = "회사명은 필수 입력 값입니다.")
    private String companyname;
    @NotEmpty(message = "사업자번호는 필수 입력 값입니다.")
    private String companynum;
    @NotEmpty(message = "대표 이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String companyemail;
    @NotEmpty(message = "회사구분은 필수 입력 값입니다.")
    private String companystand;
    private String repname;
    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String companypostcode;
    private String companyaddress1;
    @NotEmpty(message = "상세주소는 필수 입력 값입니다.")
    private String companyaddress2;
    @NotEmpty(message = "대표번호는 필수 입력 값입니다.")
    private String companyphone;
    private LocalDateTime companystart;
    private String companyfax;
    @NotEmpty(message = "대표계좌은행은 필수 입력 값입니다.")
    private String companyaccbank;
    @NotEmpty(message = "대표계좌번호는 필수 입력 값입니다.")
    private String companyaccnum;
    @NotEmpty(message = "대표계좌주명은 필수 입력 값입니다.")
    private String companyaccname;
    private static ModelMapper modelMapper = new ModelMapper();
    public static CompanyFormDto of(Company company){
        return modelMapper.map(company, CompanyFormDto.class);
    }
}

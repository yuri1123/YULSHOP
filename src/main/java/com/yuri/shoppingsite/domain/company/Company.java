package com.yuri.shoppingsite.domain.company;

import com.yuri.shoppingsite.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name="company")
public class Company extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String companyname; //회사명
    @Column(unique = true)
    private String companynum; //사업자번호
    private String companystand; //사업자유형 법인,개인
    private String repname; //대표자명
    private String companypostcode; //우편번호
    private String companyaddress1; //회사주소 1
    private String companyaddress2; //회사주소 2
    private String companyphone; //대표번호
    private String companyemail; //대표이메일
    private LocalDateTime companystart; //개업연월일
    private String companyfax; //팩스번호
    private String companyaccbank; //대표계좌은행
    private String companyaccnum; //대표계좌은행
    private String companyaccname; //대표계좌은행

    public static Company createCompany(CompanyFormDto companyFormDto){
        Company company = new Company();
        company.setCompanyname(companyFormDto.getCompanyname());
        company.setCompanynum(companyFormDto.getCompanynum());
        company.setCompanystand(companyFormDto.getCompanystand());
        company.setRepname(companyFormDto.getRepname());
        company.setCompanypostcode(companyFormDto.getCompanypostcode());
        company.setCompanyaddress1(companyFormDto.getCompanyaddress1());
        company.setCompanyaddress2(companyFormDto.getCompanyaddress2());
        company.setCompanyphone(companyFormDto.getCompanyphone());
        company.setCompanyemail(companyFormDto.getCompanyemail());
        company.setCompanystart(companyFormDto.getCompanystart());
        company.setCompanyfax(companyFormDto.getCompanyfax());
        company.setCompanyaccbank(companyFormDto.getCompanyaccbank());
        company.setCompanyaccnum(companyFormDto.getCompanyaccnum());
        company.setCompanyaccname(companyFormDto.getCompanyaccname());
        return company;
    }

    public void updateCompany(CompanyFormDto companyFormDto){
        this.companyname = companyFormDto.getCompanyname();
        this.companynum = companyFormDto.getCompanynum();
        this.companystand = companyFormDto.getCompanystand();
        this.repname = companyFormDto.getRepname();
        this.companypostcode = companyFormDto.getCompanypostcode();
        this.companyaddress1 = companyFormDto.getCompanyaddress1();
        this.companyaddress2 = companyFormDto.getCompanyaddress2();
        this.companyphone = companyFormDto.getCompanyphone();
        this.companyemail = companyFormDto.getCompanyemail();
        this.companystart = companyFormDto.getCompanystart();
        this.companyfax = companyFormDto.getCompanyfax();
        this.companyaccbank = companyFormDto.getCompanyaccbank();
        this.companyaccnum = companyFormDto.getCompanyaccnum();
        this.companyaccname = companyFormDto.getCompanyaccname();
    }

}

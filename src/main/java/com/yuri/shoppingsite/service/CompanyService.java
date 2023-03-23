package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.CompanyRepository;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.company.CompanyFormDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public CompanyFormDto getCompanyForm(String companyname){
        Company company = companyRepository.findbycompanyname(companyname);
        CompanyFormDto companyFormDto = CompanyFormDto.of(company);
        return companyFormDto;
    }




}

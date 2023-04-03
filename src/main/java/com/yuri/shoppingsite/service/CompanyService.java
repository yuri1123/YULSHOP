package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.CompanyRepository;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.company.CompanyFormDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberAccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public CompanyFormDto getCompanyForm(String companyname) {
        Company company = companyRepository.findbycompanyname(companyname);
        CompanyFormDto companyFormDto = CompanyFormDto.of(company);
        return companyFormDto;
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public void updateCompany(CompanyFormDto companyFormDto) {
        Company company = companyRepository.findById(companyFormDto.getId()).orElse(null);
        if (company != null) {
            modelMapper.map(companyFormDto, company);
            companyRepository.save(company);
        }
    }


    public CompanyFormDto findbyFirstId() {
        List<Company> companyList = companyRepository.findAllcompany();
        Long id = companyList.get(0).getId();
        Company companyInfo = companyRepository.findByid(id);
        CompanyFormDto companyFormDto = CompanyFormDto.of(companyInfo);
        return companyFormDto;
    }

    public List<Company> getcompanyList(){
        List<Company> companyList = companyRepository.findAllcompany();
        return companyList;
    }
}
package com.yuri.shoppingsite.controller;

import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.shop.BestSellerItemDto;
import com.yuri.shoppingsite.domain.shop.ItemSearchDto;
import com.yuri.shoppingsite.service.CompanyService;
import com.yuri.shoppingsite.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
   ItemService itemService;

    @Autowired
    CompanyService companyService;
    @GetMapping("/")
    public String home(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);
        Page<BestSellerItemDto> items = itemService.getBestSellerItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 1);
        List<Company> companyList = companyService.getcompanyList();
        Company company = companyList.get(0);
        System.out.println(company);

        model.addAttribute("company",company);

        return "index";
    }


}


//

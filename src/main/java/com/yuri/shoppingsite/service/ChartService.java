package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.MonthlySalesRepository;
import com.yuri.shoppingsite.domain.shop.MonthlySales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartService {

    @Autowired
    MonthlySalesRepository monthlySalesRepository;

    //월별 매출액 조회
    public List<MonthlySales> getMonthlySales(String year){
        return monthlySalesRepository.findByDateStartsWith(year);
    }
}

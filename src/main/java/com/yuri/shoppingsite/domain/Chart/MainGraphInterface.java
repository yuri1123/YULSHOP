package com.yuri.shoppingsite.domain.Chart;

import java.time.LocalDateTime;

public interface MainGraphInterface {

        Integer getCount();
        Integer getOrder_price();
        String getReg_time();


//    @QueryProjection
//    public MainGraphDto(int sumSelling, int sumIncome,
//                        LocalDateTime standardDate){
//        this.sumSelling = sumSelling;
//        this.sumIncome = sumIncome;
//        this.standardDate = standardDate;
//    }
}
package com.yuri.shoppingsite.domain.chart;

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
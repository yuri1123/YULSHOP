package com.yuri.shoppingsite.domain.Chart;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class MainGraphDto {

        private int sumSelling;
        private int sumIncome;
        private LocalDateTime standardDate;


    @QueryProjection
    public MainGraphDto(int sumSelling, int sumIncome,
                        LocalDateTime standardDate){
        this.sumSelling = sumSelling;
        this.sumIncome = sumIncome;
        this.standardDate = standardDate;
    }
}
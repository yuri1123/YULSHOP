package com.yuri.shoppingsite.domain.shop;

import com.querydsl.core.annotations.QueryProjection;
import com.yuri.shoppingsite.constant.Category;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResultCategoryItemDto {

    private Category category;
    private int sumCount;
    private int sumIncome;
    @QueryProjection
    public ResultCategoryItemDto(Category category,
                                 int sumCount, int sumIncome){
        this.category = category;
        this.sumCount = sumCount;
        this.sumIncome = sumIncome;
    }
}
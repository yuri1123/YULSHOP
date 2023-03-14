package com.yuri.shoppingsite.domain.shop;

import com.querydsl.core.annotations.QueryProjection;
import com.yuri.shoppingsite.constant.Category;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class CategoryItemsDto {

    private Category category;
    private int totalIncome;

    @QueryProjection
    public CategoryItemsDto(Category category, int totalIncome){
        this.category = category;
        this.totalIncome = totalIncome;
    }
}



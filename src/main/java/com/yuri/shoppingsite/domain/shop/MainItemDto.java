package com.yuri.shoppingsite.domain.shop;

import com.querydsl.core.annotations.QueryProjection;
import com.yuri.shoppingsite.constant.Category;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainItemDto {

    private Long id;
    private Category category;
    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection
    public MainItemDto(Long id, Category category, String itemNm, String itemDetail, String imgUrl,Integer price){
        this.id = id;
        this.category = category;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
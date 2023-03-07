package com.yuri.shoppingsite.domain.shop;

import com.querydsl.core.annotations.QueryProjection;
import com.yuri.shoppingsite.constant.Category;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResultSellingItemDto {

    private Long id;
    private Category category;
    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    private int orderTotalCount; //주문총횟수
    private int orderTotalIncome; //총수익
    @QueryProjection
    public ResultSellingItemDto(Long id, String itemNm, Category category,String itemDetail, String imgUrl, Integer price, Integer orderTotalCount, Integer orderTotalIncome){
        this.id = id;
        this.itemNm = itemNm;
        this.category = category;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.orderTotalCount = orderTotalCount;
        this.orderTotalIncome = orderTotalIncome;
    }
}
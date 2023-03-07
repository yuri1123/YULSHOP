package com.yuri.shoppingsite.domain.shop;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LatestItemDto {

    private Long id;
    private String category;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    private Integer orderTotalCount; //주문총횟수
    @QueryProjection
    public LatestItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price, Integer orderTotalCount){
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.orderTotalCount = getOrderTotalCount();
    }
}
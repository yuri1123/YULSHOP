package com.yuri.shoppingsite.domain.shop;

import com.querydsl.core.annotations.QueryProjection;
import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class MainItemDto {

    private Long id;
    private Category category;
    private String itemNm;

    private String itemDetail;

    private String imgUrl;
    private int stockNumber;

    private Integer price;
    private ItemSellStatus itemSellStatus;
    private LocalDateTime regTime;
    private String createdBy;
    private Integer addCount;

    private Integer orderTotalCount;
    @QueryProjection
    public MainItemDto(Long id, Category category, String itemNm, String itemDetail, String imgUrl,Integer price,
                       int stockNumber, ItemSellStatus itemSellStatus,LocalDateTime regTime, int addCount,
                       String createdBy,int orderTotalCount){
        this.id = id;
        this.category = category;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.stockNumber = stockNumber;
        this.itemSellStatus = itemSellStatus;
        this.regTime = regTime;
        this.addCount = addCount;
        this.createdBy = createdBy;
        this.orderTotalCount = orderTotalCount;
    }
}
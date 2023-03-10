package com.yuri.shoppingsite.domain.shop;

import com.yuri.shoppingsite.constant.Category;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderItemDto {

    //OrderItemDto 클래스의 생성자로 orderItem 객체와 이미지 경로를 파라미터로 받아서 멤버 변수 값을 셋팅한다.
    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
        this.category = category;
        this.regTime = regTime;
    }

    private Category category;
    private String itemNm; //상품명
    private int count; //주문수량
    private int orderPrice; // 주문 금액
    private String imgUrl; // 상품 이미지 경로
    private LocalDateTime regTime;

}

package com.yuri.shoppingsite.domain.shop;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class CartOrderDto {

    private Long cartItemId;
    //장바구니에서 여러개의 상품 주문하므로 CartOrderDto 클래스가 자기 자신을 List로 가지고 있도록 한다.
    private List<CartOrderDto> cartOrderDtoList;

}

package com.yuri.shoppingsite.domain.shop;

import com.yuri.shoppingsite.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderHistDto {

    //order 객체를 파라미터로 받아서 멤버 변수 값을 셋팅한다. 주문날짜의 경우 화면에
    // yyyy-mm-dd hh:mm 형태로 전달하기 위해 포맷을 수정한다.
    public OrderHistDto(Order order){
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId; //주문아이디
    private String orderDate; //주문날짜
    private OrderStatus orderStatus; //주문상태

    //주문상품 리스트
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    //orderItemDto 객체를 주문 상품 리스트에 추가하는 메소드
    public void addOrderItemDto(OrderItemDto orderItemDto){
        orderItemDtoList.add(orderItemDto);
    }

}

package com.yuri.shoppingsite.domain.shop;


import com.yuri.shoppingsite.constant.OrderStatus;
import com.yuri.shoppingsite.domain.common.BaseEntity;
import com.yuri.shoppingsite.domain.user.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="orders") //정렬시에도 order를 사용하므로 orders로 표시하였음
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    //한명의 회원은 여러번 주문할 수 있으므로 주문 엔티티 기준에서 다대일 단방향 매핑을 한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    //부모엔티티의 영속성 상태 변화를 자식 엔티티에 모두 전이하는 CascadeTypeAll옵션을 설정하겠다
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
                        orphanRemoval = true, fetch = FetchType.LAZY)
    //하나의 주문이 여러개의 주문 상품을 갖으므로 List 자료형을 사용해서 매핑한다.
    private List<OrderItem> orderItems = new ArrayList<>();

//    private LocalDateTime regDTime;
//    private LocalDateTime updateTime;

    //orderItems에는 주문 상품 정보들을 담아준다. orderItem 객체를 order객체의 orderItems에 추가한다.
    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        //Order 엔티티와 OrderItem엔티티가 양방향 참조 관계이므로, orderItem 객체에도 order 객체를 셋팅한다.
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member);
        //상품을 주문한 회원의 정보를 셋팅한다.
        for(OrderItem orderItem : orderItemList){
            //상품 페이지에서는 1개의 상품을 주문하지만 장바구니 페이지에서는 한번에 여러 개의 상품을 주문할 수 있다.
            //따라서 여러 개의 주문상품을 담을 수 있도록 리스트 형태로 파라미터 값을 받으면서 주문 객체에 orderItem 객체를 추가한다.
        order.addOrderItem(orderItem);
        }
        //주문상태를 "ORDER"로 셋팅한다.
        order.setOrderStatus(OrderStatus.ORDER);
        //현재시간을 주문 시간으로 셋팅한다.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }
    //총 주문 금액을 구하는 메소드
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem: orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }



}

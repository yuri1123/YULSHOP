package com.yuri.shoppingsite.domain.shop;

import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import com.yuri.shoppingsite.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    private Category category;
    //하나의 상품은 여러 주문 상품으로 들어갈 수 있으므로 주문 상품 기준으로 단방향 매핑을 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private String itemNm;

    //한번의 주문에는 여러 개의 상품을 주문할 수 있으므로 주문 상품 엔티티와 주문 엔티티를 다대일 단방향 매핑을 먼저 설정한다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;
    private int orderPrice; //주문가격
    private int count; //수량



//    private LocalDateTime regTime;
//    private LocalDateTime updateTime;

    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        //주문할 상품과 주문 수량을 셋팅한다.
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setCategory(Category.valueOf(item.getCategory().toString()));
        orderItem.setItemNm(item.getItemNm());
        //현재 시간 기준으로 상품 가격을 주문가격으로 셋팅한다.
        //상품 가격은 시간에 따라서 달라질 수 있다.
        //또한 쿠폰이나 할인을 적용하는 케이스들도 있지만 여기서는 고려하지 않겠다.
        orderItem.setOrderPrice(item.getPrice());
        //주문 수량만큼 상품의 재고 수량을 감소시킨다.
        item.removeStock(count);
        item.addOrderTotalCount(count);
        item.addOrderTotalIncome(item.getPrice() * count);
        if(item.getStockNumber() == 0){
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
        }
        return orderItem;
    }
    //주문 가격과 주문 수량을 곱해서 해당 상품을 주문한 총 가격을 계산하는 메소드이다.
    public int getTotalPrice(){
        return orderPrice*count;
    }

    public void cancel(){

        this.getItem().addStock(count);
        this.getItem().removeOrderCount(count);
        this.getItem().removeOrderTotalIncome(item.getPrice() * count);

        if(item.getStockNumber() != 0){
            item.setItemSellStatus(ItemSellStatus.SELL);
        }

    }

    }


package com.yuri.shoppingsite.domain.shop;

import com.yuri.shoppingsite.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name="cart_item_id")
    private Long id;

    //하나의 장바구니에 여러 개의 상품을 담을 수 있으므로 @ManyTOOne 어노테이션을 이용해서
    //다대일 관계로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    //장바구니에 담을 상품의 정보를 알아야 하므로 상품 엔티티를 매핑한다
   //하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있으므로 @ManyToOne 어노테이션을 이용하여 다대일 관계로 매핑한다
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    //같은 상품을 장바구니에 몇개 담을지 지정한다
    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }
    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }

}

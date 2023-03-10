package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

//    @Query("select o from OrderItem o order by o.regTime desc limit 12")
//    List<OrderItem> latestSellingitems();
}

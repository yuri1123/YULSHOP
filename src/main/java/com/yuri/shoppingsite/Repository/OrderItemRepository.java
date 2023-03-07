package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.OrderItem;
import com.yuri.shoppingsite.domain.shop.OrderItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Transactional
    @Query("select o from OrderItem o order by o.regTime desc")
    public List<OrderItem> findAll(@Param("regTime") LocalDateTime regTime);



}
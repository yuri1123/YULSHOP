package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.chart.MainGraphInterface;
import com.yuri.shoppingsite.domain.shop.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Transactional
    @Query("select o from OrderItem o order by o.regTime asc")
    public List<OrderItem> findAll(@Param("regTime") LocalDateTime regTime);

    @Query(value="select sum(oi.count) as count," +
            " sum(oi.order_price) as order_price, date_format(oi.reg_time, '%Y-%m') as reg_time" +
            " from OrderItem oi group by date_format(oi.reg_time, '%Y-%m') order by reg_time asc;",nativeQuery = true)
    List<MainGraphInterface> mainchart();



}
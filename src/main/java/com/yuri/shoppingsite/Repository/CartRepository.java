package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.Cart;
import com.yuri.shoppingsite.domain.shop.CartDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart,Long> {

        Cart findByMemberId(Long memberId);


}


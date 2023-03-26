package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.CartDetailDto;
import com.yuri.shoppingsite.domain.shop.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    //CartDetailDto의 생성자를 이용하여 DTO를 반환할 때는  new키워드와 해당 DTO의
    //패키지, 클래스명을 적어준다. 또한 생성자의 파라미터 순서는
    // DTO 클래스에 명시한 순으로 넣어줘야한다.
    //장바구니에 담겨있는 상품으 ㅣ대표 이미지만 가지고 오도록 조건문을 작성한다.
    @Query("select new com.yuri.shoppingsite.domain.shop.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, BoardImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}

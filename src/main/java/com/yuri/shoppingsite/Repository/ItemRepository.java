package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//JpaRepository는 2개의 제네릭 타입을 사용하는데
//첫번째는 엔티티 타입 클래스를 넣어주고
//두번째는 기본키 타입을 넣음

public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    //itemNm(상품명)으로 데이터를 조회하기 위해 By 뒤에 필드명인 itemNm을 메소드의 이름에 붙여준다
    //엔티티명은 생략이 가능하므로 findItemByItemNm 대신에 findByItemNm으로 메소드명을 만들어주고,
    //매개 변수로는 검색할 때 사용할 상품명 변수를 넘겨준다.

    //상품명으로 조회하기
    List<Item> findByItemNm(String itemNm);
    //상품명 OR 상품내용으로 조회하기
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    //가격이하인 상품 찾기
    List<Item> findByPriceLessThan(Integer price);
    //가격이하인 상품을 찾고 가격 역순으로 정렬하기
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    //상품상세설명을 파라미터로 받아 해당 내용을 상품 상세설명에 포함하고 있는 데이터를 조회, 가격높은순 조회
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    //navtivequery를 그대로 사용하여 상세내용 검색하기
    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc",nativeQuery = true)
    List<Item> findByItemDetailByNativ(@Param("itemDetail") String itemDetail);


}

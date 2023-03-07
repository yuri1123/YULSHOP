//package com.yuri.shoppingsite;

import com.yuri.shoppingsite.Repository.ItemRepository;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import com.yuri.shoppingsite.domain.shop.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

//import java.time.LocalDateTime;
//@SpringBootTest //통합테스트를 위해서 제공하는 어노테이션, 실제 구동처럼 모든 Bean을 IoC 컨테이너에 등록함
//@TestPropertySource(locations = "classpath:application-test.properties") //application.properties이외에 설정이 있다면 더 높은 우선순위 부여
//class ItemRepositoryTest {
//
//    @Autowired //Bean을 주입
//    ItemRepository itemRepository;
//
//    @Test //테스트할 메소드 위에 Test를 붙임
//    @DisplayName("상품 저장 테스트") //지정한 테스트명이 노출됨
//    public void createItemTest(){
//        Item item = new Item();
//        item.setItemNm("테스트 상품");
//        item.setPrice(10000);
//        item.setItemDetail("내용입니다");
//        item.setItemSellStatus(ItemSellStatus.SELL);
//        item.setStockNumber(100);
//        item.setRegTime(LocalDateTime.now());
//        item.setUpdateTime(LocalDateTime.now());
//        Item savedItem = itemRepository.save(item);
//        System.out.println(savedItem.toString());
//    }


//}
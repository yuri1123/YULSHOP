package com.yuri.shoppingsite.Repository;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import com.yuri.shoppingsite.domain.shop.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest //통합테스트를 위해서 제공하는 어노테이션, 실제 구동처럼 모든 Bean을 IoC 컨테이너에 등록함
@TestPropertySource(locations = "classpath:application-test.properties") //application.properties이외에 설정이 있다면 더 높은 우선순위 부여
class ItemRepositoryTest {

    @Autowired //Bean을 주입
    ItemRepository itemRepository;

    @Test //테스트할 메소드 위에 Test를 붙임
    @DisplayName("상품 저장 테스트") //지정한 테스트명이 노출됨
    public void createItemTest() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("내용입니다");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    //findByItemNmTest()를 위해서는 데이터가 필요하므로 인위로 넣어주는 메소드를 만들었음
    public void createItemList(){
        for(int i=1; i<10; i++){
            Item item = new Item();
            item.setItemNm("테스트상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("내용입니다"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        //테스트상품1이라는 이름을 검색한 자료를 itemList에 담고 출력
        List<Item> itemList = itemRepository.findByItemNm("테스트상품1");
        for(Item item : itemList){
           System.out.println(item.toString());
        }

    }

    @Test
    @DisplayName("상품명 조회 OR 조건 테스트")
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();
        //테스트상품1이라는 이름을 검색한 자료를 itemList에 담고 출력
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트상품1","내용입니다5");
        for(Item item : itemList){
            System.out.println(item.toString());
        }

    }
    @Test
    @DisplayName("가격이하인 조건 테스트 LessThan")
    public void findByPriceLessThanTest(){
        this.createItemList();
        //테스트상품1이라는 이름을 검색한 자료를 itemList에 담고 출력
        List<Item> itemList = itemRepository.findByPriceLessThan(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }

    }
    @Test
    @DisplayName("가격이하인 조건 + 내림차순 정렬")
    public void  findByPriceLessThanOrderByPriceDescTest(){
        this.createItemList();
        //테스트상품1이라는 이름을 검색한 자료를 itemList에 담고 출력
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10005);
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    @Test
    @DisplayName("@Query를 이용한 상품조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        //테스트상품1이라는 이름을 검색한 자료를 itemList에 담고 출력
        List<Item> itemList = itemRepository.findByItemDetail("내용입니다");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }
    @Test
    @DisplayName("nativequery 이용한 내용 검색")
    public void findByItemDetailByNativ(){
        this.createItemList();
        //테스트상품1이라는 이름을 검색한 자료를 itemList에 담고 출력
        List<Item> itemList = itemRepository.findByItemDetailByNativ("내용입니다");
        for(Item item : itemList){
            System.out.println(item.toString());
        }
    }


    
}
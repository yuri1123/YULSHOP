//package com.yuri.shoppingsite.domain.userFunction;
//
//import com.yuri.shoppingsite.Repository.*;
//import com.yuri.shoppingsite.constant.ItemSellStatus;
//import com.yuri.shoppingsite.domain.shop.Item;
//import com.yuri.shoppingsite.domain.user.Member;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityNotFoundException;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-test.properties")
//class OrderTest {
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @PersistenceContext
//    EntityManager em;
//
//    public Item createItem(){
//        Item item = new Item();
//        item.setItemNm("테스트상품");
//        item.setPrice(10000);
//        item.setItemDetail("상세설명");
//        item.setItemSellStatus(ItemSellStatus.SELL);
//        item.setStockNumber(100);
//        item.setRegTime(LocalDateTime.now());
//        item.setUpdateTime(LocalDateTime.now());
//        return item;
//    }
//
//    @Test
//    @DisplayName("영속성 전이 테스트")
//    public void cascadeTest(){
//        Order order = new Order();
//        for(int i=0; i<3; i++){
//            Item item = this.createItem();
//            itemRepository.save(item);
//            OrderItem orderItem = new OrderItem();
//            orderItem.setItem(item);
//            orderItem.setCount(10);
//            orderItem.setOrderPrice(1000);
//            orderItem.setOrder(order);
//            //영속성 컨텍스트에 저장되지 않은 orderItem 엔티티를 order 엔티티에 담아준다
//            order.getOrderItems().add(orderItem);
//        }
//        //order 엔티티를 저장하면서 강제로 flush를 호출하여 영속성 컨텍스트에 있는 객체들을 데이터베이스에 반영한다.
//    orderRepository.saveAndFlush(order);
//        //영속성 컨텍스트의 상태를 초기화한다.
//        em.clear();
//        //영속성 컨텍스트를 초기화했기 때문에 데이터베이스에서 주문 엔티티를 조회한다.
//        //select 쿼리문이 실행되는것을 콘솔창에서 확인할 수 있다.
//        Order savedOrder = orderRepository.findById(order.getId())
//                .orElseThrow(EntityNotFoundException::new);
//        //엔티티 3개가 실제로 db에 저장되어있는지 검사한다.
//        assertEquals(3, savedOrder.getOrderItems().size());
//    }
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    //주문 데이터를 생성해서 저장하는 메소드
//    public Order createOrder(){
//        Order order = new Order();
//
//        for(int i=0; i<3; i++){
//            Item item = createItem();
//            itemRepository.save(item);
//            OrderItem orderItem = new OrderItem();
//            orderItem.setItem(item);
//            orderItem.setCount(10);
//            orderItem.setOrderPrice(1000);
//            orderItem.setOrder(order);
//            order.getOrderItems().add(orderItem);
//        }
//
//        Member member = new Member();
//        memberRepository.save(member);
//
//        order.setMember(member);
//        memberRepository.save(member);
//
//        order.setMember(member);
//        orderRepository.save(order);
//        return order;
//    }
//
//    //order 엔티티에서 관리하고 있는 orderItem 리스트의 0번째 인덱스 요소를 제거한다.
//    @Test
//    @DisplayName("고아객체 제거 테스트")
//    public void orphanRemovalTest(){
//        Order order = this.createOrder();
//        order.getOrderItems().remove(0);
//        em.flush();
//    }
//
//
//    @Autowired
//    OrderItemRepository orderItemRepository;
//
//    @Test
//    @DisplayName("지연로딩 테스트")
//    public void lazyLoadingTest(){
//        Order order = this.createOrder();
//        Long orderItemId = order.getOrderItems().get(0).getId();
//        em.flush();
//        em.clear();
//
//        OrderItem orderItem = orderItemRepository.findById(orderItemId)
//                .orElseThrow(EntityNotFoundException::new);
//        System.out.println("Order class : "+orderItem.getOrder().getClass());
//        System.out.println("================================================");
//        orderItem.getOrder().getOrderDate();
//        System.out.println("================================================");
//    }
//
//
//}
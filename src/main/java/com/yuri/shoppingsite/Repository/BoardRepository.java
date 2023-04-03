package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import com.yuri.shoppingsite.domain.chart.CategoryItemsInterface;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.CommunitySearchDto;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.shop.Item;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

//JpaRepository는 2개의 제네릭 타입을 사용하는데
//첫번째는 엔티티 타입 클래스를 넣어주고
//두번째는 기본키 타입을 넣음

public interface BoardRepository extends JpaRepository<Board, Long>,
        QuerydslPredicateExecutor<Board>, BoardRepositoryCustom {


    //notice
    @Query("select b from Board b where b.type='NOTICE'")
    List<Board> findallNotice();

    //공지사항 id로 찾기
    @Query("select b from Board b where b.type='NOTICE' and b.id=:id")
    Board findallNoticebyid(@Param("id") Long id);

    //리뷰글 id로 찾기
    @Query("select b from Board b where b.type='REVIEW' and b.id=:id")
    Board findallReviewbyId(@Param("id") Long id);

    //질의글 id로 찾기
    @Query("select b from Board b where b.type='QNA' and b.id=:id")
    Board findallQnabyId(@Param("id") Long id);

    @Query("select b from Board b where b.id=:id")
    Board findByid(Long id);

    @Query("select b from Board b where b.createdBy=:name")
    List<Board> findByCreatedBy(@Param("name") String name);


}

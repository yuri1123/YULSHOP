package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.community.Answer;
import com.yuri.shoppingsite.domain.community.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    //공지사항 id로 찾기
    @Query("select a from Answer a where a.id=:id")
    Answer findByid(@Param("id") Long id);

    @Query("select a from Answer a where a.board=:bid")
    List<Answer> findByBoardId(@Param("bid") Long bid);

    List<Answer> findByBoard(Board board);
}

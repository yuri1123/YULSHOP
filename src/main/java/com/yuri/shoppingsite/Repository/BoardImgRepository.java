package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.community.BoardImg;
import com.yuri.shoppingsite.domain.community.BoardImgDto;
import com.yuri.shoppingsite.domain.shop.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardImgRepository extends JpaRepository<BoardImg, Long> {

//    List<BoardImg> findByBoardIdOrderByIdAsc(Long boardId);
//
//    BoardImg findByBoardIdAndRepimgYn(Long boardId, String repimgYn);

}

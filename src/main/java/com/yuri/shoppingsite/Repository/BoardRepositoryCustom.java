package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.CommunitySearchDto;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> getBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable);
    Page<Board> getNoticeBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable);
    Page<Board> getReviewBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable);
    Page<Board> getQnaBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable);
    Page<Board> getMyBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable
    ,String name);
}

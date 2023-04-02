package com.yuri.shoppingsite.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.constant.Type;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.CommunitySearchDto;
import com.yuri.shoppingsite.domain.community.QBoard;
import com.yuri.shoppingsite.domain.shop.QItem;
import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.MemberSearchDto;
import com.yuri.shoppingsite.domain.user.QMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

        private JPAQueryFactory queryFactory;
        public BoardRepositoryCustomImpl(EntityManager em) {
            this.queryFactory = new JPAQueryFactory(em);
        }


        private BooleanExpression regDtsAfter(String searchDateType){
            LocalDateTime dateTime = LocalDateTime.now();

            if(StringUtils.equals("all", searchDateType) || searchDateType == null){
                return null;
            } else if(StringUtils.equals(("1d"),searchDateType)){
                dateTime = dateTime.minusDays(1);
            } else if(StringUtils.equals(("1w"), searchDateType)){
                dateTime = dateTime.minusWeeks(1);
            } else if(StringUtils.equals(("1m"),searchDateType)){
                dateTime = dateTime.minusMonths(1);
            } else if(StringUtils.equals(("6m"),searchDateType)){
                dateTime = dateTime.minusMonths(6);
            }
            return QBoard.board.regTime.after(dateTime);
        }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemNm", searchBy)){
            return QBoard.board.itemNm.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QBoard.board.createdBy.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("title", searchBy)){
            return QBoard.board.title.like("%"+searchQuery+"%");
        }
        return null;
    }
    private BooleanExpression searchTypeStatusEq(String searchTypeStatus) {

        if (StringUtils.equals("ALL", searchTypeStatus) || searchTypeStatus == null) {
            return null;
        } else if (StringUtils.equals("NOTICE", searchTypeStatus)) {
            return QBoard.board.type.eq(searchTypeStatus);
        } else if (StringUtils.equals("QNA", searchTypeStatus)) {
            return QBoard.board.type.eq(searchTypeStatus);
        } else if (StringUtils.equals("REVIEW", searchTypeStatus)) {
            return QBoard.board.type.eq(searchTypeStatus);
        }
        return null;
    }

    //전체게시판 + 페이징 + 검색
    @Override
    public Page<Board> getBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable){

            List<Board> content = queryFactory
                    .selectFrom(QBoard.board)
                    .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                            searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                            searchByLike(communitySearchDto.getSearchBy(),
                                    communitySearchDto.getSearchQuery()))
                    .orderBy(QBoard.board.id.desc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
            long total = queryFactory.select(Wildcard.count).from(QBoard.board)
                    .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                            searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                            searchByLike(communitySearchDto.getSearchBy(),
                                    communitySearchDto.getSearchQuery()))
                    .fetchOne();

            return new PageImpl<>(content, pageable, total);
    }
    //공지사항 + 페이징 + 검색
    private BooleanExpression nameLike(String searchQuery){
            return StringUtils.isEmpty(searchQuery) ?
                    null : QMember.member.name.like("%"+searchQuery+"%");
    }

    @Override
    public Page<Board> getNoticeBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable){

        List<Board> content = queryFactory
                .selectFrom(QBoard.board)
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .where(QBoard.board.type.eq("NOTICE"))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(Wildcard.count).from(QBoard.board)
                .where(QBoard.board.type.eq("NOTICE"))
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<Board> getReviewBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable){

        List<Board> content = queryFactory
                .selectFrom(QBoard.board)
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .where(QBoard.board.type.eq("REVIEW"))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(Wildcard.count).from(QBoard.board)
                .where(QBoard.board.type.eq("REVIEW"))
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<Board> getQnaBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable){

        List<Board> content = queryFactory
                .selectFrom(QBoard.board)
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .where(QBoard.board.type.eq("QNA"))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(Wildcard.count).from(QBoard.board)
                .where(QBoard.board.type.eq("QNA"))
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Board> getMyBoardPage(CommunitySearchDto communitySearchDto, Pageable pageable,
                                      String name){

        List<Board> content = queryFactory
                .selectFrom(QBoard.board)
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .where(QBoard.board.createdBy.eq(name))
                .orderBy(QBoard.board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = queryFactory.select(Wildcard.count).from(QBoard.board)
                .where(QBoard.board.createdBy.eq(name))
                .where(regDtsAfter(communitySearchDto.getSearchDateType()),
                        searchTypeStatusEq(communitySearchDto.getSearchTypeStatus()),
                        searchByLike(communitySearchDto.getSearchBy(),
                                communitySearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}






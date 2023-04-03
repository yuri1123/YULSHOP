package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.BoardRepository;
import com.yuri.shoppingsite.domain.community.*;
import com.yuri.shoppingsite.domain.shop.Item;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import com.yuri.shoppingsite.domain.shop.ItemImg;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public NoticeFormDto getNoticeForm() {
        Board board = boardRepository.findallNoticebyid(getNoticeForm().getId());
        NoticeFormDto noticeFormDto = NoticeFormDto.of(board);
        return noticeFormDto;
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public void createNotice(NoticeFormDto noticeFormDto){
        Board board = modelMapper.map(noticeFormDto, Board.class);
        boardRepository.save(board);
        }

    //공지사항 수정
    public void updateNotice(NoticeFormDto noticeFormDto) {
        Board board = boardRepository.findById(noticeFormDto.getId()).orElse(null);
        if (board != null) {
            modelMapper.map(noticeFormDto, board);
            boardRepository.save(board);
        }
    }
    //리뷰 수정
    public void updateReview(ReviewFormDto reviewFormDto) {
        Board board = boardRepository.findById(reviewFormDto.getId()).orElse(null);
        if (board != null) {
            modelMapper.map(reviewFormDto, board);
            boardRepository.save(board);
        }
    }
    //문의 수정
    public void updateQna(QnaFormDto qnaFormDto) {
        Board board = boardRepository.findById(qnaFormDto.getId()).orElse(null);
        if (board != null) {
            modelMapper.map(qnaFormDto, board);
            boardRepository.save(board);
        }
    }

    public List<Board> getNotice(){
        List<Board> notice = boardRepository.findallNotice();
        return notice;
    }

    //공지글 id로 찾기
    public Board getNoticeById(Long id){
        Board notice = boardRepository.findallNoticebyid(id);
        return notice;
    }
    //공지글 id로 찾기
    public Board getReviewById(Long id){
        Board review = boardRepository.findallReviewbyId(id);
        return review;
    }
    //공지글 id로 찾기
    public Board getQnaById(Long id){
        Board qna = boardRepository.findallQnabyId(id);
        return qna;
    }

    //noticeformdto 가져오기
    public NoticeFormDto getNoticeFormbyId(Long id){
        Board board = boardRepository.findallNoticebyid(id);
        NoticeFormDto noticeFormDto = NoticeFormDto.of(board);
        return noticeFormDto;
    }
    //reviewformdto 가져오기
    public ReviewFormDto getReviewFormbyId(Long id){
        Board board = boardRepository.findallReviewbyId(id);
        ReviewFormDto reviewFormDto = ReviewFormDto.of(board);
        return reviewFormDto;
    }
    //qnaformdto 가져오기
    public QnaFormDto getQnaFormbyId(Long id){
        Board board = boardRepository.findallQnabyId(id);
        QnaFormDto qnaFormDto = QnaFormDto.of(board);
        return qnaFormDto;
    }

    //내가쓴 게시글 가져오기, 페이징, 검색
    public Page<Board> getMyBoardList(CommunitySearchDto communitySearchDto, Pageable pageable,
                                      String name){
        return boardRepository.getMyBoardPage(communitySearchDto, pageable, name);
    }

    //공지사항 게시글 가져오기, 페이징, 검색
    public Page<Board> getNoticeBoardList(CommunitySearchDto communitySearchDto, Pageable pageable){
        return boardRepository.getNoticeBoardPage(communitySearchDto, pageable);
    }

    //리뷰게시판 게시글 가져오기, 페이징, 검색
    public Page<Board> getReviewBoardList(CommunitySearchDto communitySearchDto, Pageable pageable){
        return boardRepository.getReviewBoardPage(communitySearchDto, pageable);
    }
    //qna 게시글 가져오기, 페이징, 검색
    public Page<Board> getQnaBoardList(CommunitySearchDto communitySearchDto, Pageable pageable){
        return boardRepository.getQnaBoardPage(communitySearchDto, pageable);
    }

    //리뷰게시글 등록하기
    public void createReview(ReviewFormDto reviewFormDto){
        Board board = modelMapper.map(reviewFormDto, Board.class);
        boardRepository.save(board);
    }
    //qna 등록하기
    public void createQna(QnaFormDto qnaFormDto){
        Board board = modelMapper.map(qnaFormDto, Board.class);
        boardRepository.save(board);
    }

}

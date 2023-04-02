package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.BoardRepository;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.CommunitySearchDto;
import com.yuri.shoppingsite.domain.community.NoticeFormDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public void updateNotice(NoticeFormDto noticeFormDto) {
        Board board = boardRepository.findById(noticeFormDto.getId()).orElse(null);
        if (board != null) {
            modelMapper.map(noticeFormDto, board);
            boardRepository.save(board);
        }
    }

    public List<Board> getNotice(){
        List<Board> notice = boardRepository.findallNotice();
        return notice;
    }

    public Board getNoticebyId(Long id){
        Board notice = boardRepository.findallNoticebyid(id);
        return notice;
    }

    public NoticeFormDto getNoticeFormbyId(Long id){
        Board board = boardRepository.findallNoticebyid(id);
        NoticeFormDto noticeFormDto = NoticeFormDto.of(board);
        return noticeFormDto;
    }

//    //회원이 쓴 게시글 보기
//    public List<Board> getMyBoardList(String name){
//        List<Board> boardList = boardRepository.findByCreatedBy(name);
//    return boardList;
//    }

    //내가쓴 게시글 가져오기, 페이징, 검색
    public Page<Board> getMyBoardList(CommunitySearchDto communitySearchDto, Pageable pageable,
                                      String name){
        return boardRepository.getMyBoardPage(communitySearchDto, pageable, name);
    }

}

package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.BoardRepository;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.NoticeFormDto;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.company.CompanyFormDto;
import com.yuri.shoppingsite.domain.shop.Item;
import com.yuri.shoppingsite.domain.shop.ItemFormDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

}

package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.BoardRepository;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.NoticeFormDto;
import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.company.CompanyFormDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    public void updateCompany(NoticeFormDto noticeFormDto) {
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



}

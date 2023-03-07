package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.domain.community.BoardDTO;
import com.yuri.shoppingsite.mapper.BoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {
    @Autowired
    BoardMapper boardMapper;

    public List<BoardDTO> listAll(){
        return boardMapper.listAll();
    }

    public BoardDTO selectOne(int bid){
        return boardMapper.selectOne(bid);
    }

    public int register(BoardDTO boardDTO){
        return boardMapper.register(boardDTO);
    }

    public int update(BoardDTO boardDTO){
        return boardMapper.update(boardDTO);
    }

    public int delete(int bid){
        return boardMapper.delete(bid);
    }

}

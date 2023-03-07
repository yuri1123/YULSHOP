package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.domain.community.Notice;
import com.yuri.shoppingsite.mapper.NoticeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    //전체보기
    public List<Notice> listAll(){
        return noticeMapper.listAll();
    }
    //선택보기
    public Notice selectOne(int noticeId){
        return noticeMapper.selectOne(noticeId);
    }
    //등록하기
    public int register(Notice notice){
        return noticeMapper.register(notice);
    }
    //수정하기
    public int update(Notice notice){
        return noticeMapper.update(notice);
    }
    //삭제하기
    public int delete(int noticeId){
        return noticeMapper.delete(noticeId);
    }
    //조회수증가
    public int updateView(int noticeId){
        return noticeMapper.updateView(noticeId);
    }
}

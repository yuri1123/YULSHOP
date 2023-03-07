package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.NoticeRepository;
import com.yuri.shoppingsite.domain.community.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    @Autowired
    NoticeRepository noticeRepository;

    public List<Notice> getNotice(){
        List<Notice> notice = noticeRepository.findAll();
        return notice;
    }


}

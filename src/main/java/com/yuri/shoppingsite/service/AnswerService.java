package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.AnswerRepository;
import com.yuri.shoppingsite.domain.community.AnswerFormDto;
import com.yuri.shoppingsite.domain.community.Board;
import com.yuri.shoppingsite.domain.community.NoticeFormDto;
import com.yuri.shoppingsite.domain.user.MemberFormDto;
import com.yuri.shoppingsite.exception.DataNotFoundException;
import com.yuri.shoppingsite.domain.community.Answer;
import com.yuri.shoppingsite.domain.user.Member;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final ModelMapper modelMapper;

    public void createAnswer(AnswerFormDto answerFormDto){
        Answer answer = modelMapper.map(answerFormDto, Answer.class);
        answerRepository.save(answer);
    }
    public void updateAnswer(AnswerFormDto answerFormDto) {
        Answer answer = answerRepository.findById(answerFormDto.getId()).orElse(null);
        if (answer != null) {
            modelMapper.map(answerFormDto, answer);
            answerRepository.save(answer);
        }
    }
    //board게시판 id로 조회하기
    public List<Answer> getAnswerList(Long id){
        List<Answer> answerList = answerRepository.findByBoardId(id);
        return answerList;
    }

}

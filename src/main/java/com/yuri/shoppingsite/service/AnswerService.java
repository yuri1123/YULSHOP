package com.yuri.shoppingsite.service;

import com.yuri.shoppingsite.Repository.AnswerRepository;
import com.yuri.shoppingsite.exception.DataNotFoundException;
import com.yuri.shoppingsite.domain.community.Answer;
import com.yuri.shoppingsite.domain.community.Question;
import com.yuri.shoppingsite.domain.user.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

       private final AnswerRepository answerRepository;

        public void create(Question question, String content, Member member){
            Answer answer = new Answer();
            answer.setContent(content);
            answer.setCreateDate(LocalDateTime.now());
            answer.setQuestion(question);
            answer.setAuthor(member);
            this.answerRepository.save(answer);
        }

        public Answer getAnswer(Integer id){
            Optional<Answer> answer = this.answerRepository.findById(id);
            if(answer.isPresent()){
                return answer.get();
            } else {
                throw new DataNotFoundException("answer not found");
            }
        }

        public void modify(Answer answer, String content){
            answer.setContent(content);
            answer.setModifyDate(LocalDateTime.now());
            this.answerRepository.save(answer);
        }

        public void delete(Answer answer){
            this.answerRepository.delete(answer);
        }

        public void vote(Answer answer, Member member){
            answer.getVoter().add(member);
            this.answerRepository.save(answer);
        }

}

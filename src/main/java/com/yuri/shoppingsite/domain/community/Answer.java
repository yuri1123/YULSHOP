package com.yuri.shoppingsite.domain.community;

import com.yuri.shoppingsite.domain.common.BaseEntity;
import com.yuri.shoppingsite.domain.user.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Answer extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(columnDefinition = "TEXT")
        private String content;
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "board_id")
        private Board board;
        private static ModelMapper modelMapper = new ModelMapper();
        public static AnswerFormDto of(Answer answer){
                return modelMapper.map(answer, AnswerFormDto.class);
        }
}

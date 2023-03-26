package com.yuri.shoppingsite.domain.community;

import com.yuri.shoppingsite.domain.common.BaseEntity;
import com.yuri.shoppingsite.domain.user.Member;
import lombok.Getter;
import lombok.Setter;
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
        private Integer id;
        @Column(columnDefinition = "TEXT")
        private String content;
        @ManyToOne
        private Board board;

}

package com.yuri.shoppingsite.domain.community;

import com.yuri.shoppingsite.domain.user.Member;
import com.yuri.shoppingsite.domain.user.SiteUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(length = 200)
        private String subject;

        @Column(columnDefinition = "TEXT")
        private String content;
        @CreatedDate
        private LocalDateTime createDate;
        @ManyToOne
        private Member author;


        private LocalDateTime modifyDate;

        @ManyToMany
        Set<SiteUser> voter;

}

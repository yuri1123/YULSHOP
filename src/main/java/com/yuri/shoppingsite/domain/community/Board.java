package com.yuri.shoppingsite.domain.community;

import com.yuri.shoppingsite.constant.Type;
import com.yuri.shoppingsite.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
public class Board extends BaseEntity {

    @Id
    @Column(name="board_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type; //게시판의 유형
    private String itemNm; //상품명(리뷰게시판)
    private String title;
    @Lob
    private String content;
    private int view;
//    @OneToMany(mappedBy = "Board", cascade = CascadeType.REMOVE)
//    private List<Answer> answerList;

}

package com.yuri.shoppingsite.domain.community;

import com.yuri.shoppingsite.domain.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@ToString
public class Notice extends BaseEntity {

    @Id
    @Column(name="notice_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String noticeTitle;
    private String noticeContent;
    private String noticeWriter;
    private int noticeView;

}

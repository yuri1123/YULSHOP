package com.yuri.shoppingsite.domain.community;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class NoticeFormDto {

    private Long id;
    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String noticeTitle;
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String noticeContent;
    private String noticeWriter;
    private int noticeView;

}

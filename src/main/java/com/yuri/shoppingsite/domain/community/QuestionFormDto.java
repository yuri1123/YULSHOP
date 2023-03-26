package com.yuri.shoppingsite.domain.community;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class QuestionFormDto {

    private Long id;
    private String type;
    @NotEmpty(message="제목은 필수항목입니다")
    @Size(max=200)
    private String title;
    @NotBlank(message = "내용은 필수 입력값입니다.")
    private String content;
    private int view;
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;

    private LocalDateTime regTime;
    private LocalDateTime updateTime;


}

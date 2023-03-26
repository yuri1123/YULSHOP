package com.yuri.shoppingsite.domain.community;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedBy;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerFormDto {

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
}

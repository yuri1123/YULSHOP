package com.yuri.shoppingsite.domain.community;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerFormDto {

    private Long id;
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;
    private Long boardid;

    private static ModelMapper modelMapper = new ModelMapper();
    public static AnswerFormDto of(Answer answer){
        return modelMapper.map(answer, AnswerFormDto.class);
    }
}

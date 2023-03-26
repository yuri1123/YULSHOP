package com.yuri.shoppingsite.domain.community;

import com.yuri.shoppingsite.domain.company.Company;
import com.yuri.shoppingsite.domain.company.CompanyFormDto;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeFormDto {

    private Long id;
    private String type;
    @NotBlank(message = "제목은 필수 입력값입니다.")
    private String title;
    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Lob
    private String content;
    private int view;
    private String createdBy;
    @LastModifiedBy
    private String modifiedBy;

    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    private static ModelMapper modelMapper = new ModelMapper();
    public static NoticeFormDto of(Board board){
        return modelMapper.map(board, NoticeFormDto.class);
    }

}

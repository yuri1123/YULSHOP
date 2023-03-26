package com.yuri.shoppingsite.domain.community;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BoardImgDto {

    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repimgYn;
    //멤버 변수로 ModelMapper 객체를 추가한다.
    private static ModelMapper modelMapper = new ModelMapper();
    //ItemImg 엔티티 객체를 파라미터로 받아서 ItemImg 객체의 자료형과 멤버변수의 이름이 같을때
    //ItemImgDto로 값을 복사해서 반환한다.
    //static 메소드로 선언해 ItemImgDto 객체를 생성하지 않아도 호출할 수 있도록 한다.
    public static BoardImgDto of(BoardImg itemImg){
        return modelMapper.map(itemImg, BoardImgDto.class);
    }
}
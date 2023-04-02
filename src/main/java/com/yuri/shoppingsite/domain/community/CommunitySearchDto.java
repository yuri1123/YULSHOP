package com.yuri.shoppingsite.domain.community;

import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.constant.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommunitySearchDto {

    //현재시간과 게시글 등록 시간을 비교
    private String searchDateType;
    //게시글의 유형을 구분
    private String searchTypeStatus;
    //상품 조회할 때 어떤 유형으로 조회할지 선택한다.
    //조회할 검색어 저장할 변수이다. searchBy가 id이면 아이디기준으로 검색하고,
    //createdBy일 경우 게시글 등록자 아이디 기준으로 검색한다.
    private String searchBy;
    private String searchQuery = "";
}

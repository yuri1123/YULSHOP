package com.yuri.shoppingsite.domain.user;

import com.yuri.shoppingsite.constant.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchDto {

    //현재시간과 회원가입일자를 비교하여 상품 데이터를 조회한다.
    private String searchDateType;
    //회원 역할 기준으로 회원 데이터 조회
    private Role searchRoleStatus;
    //상품 조회할 때 어떤 유형으로 조회할지 선택한다.
    //조회할 검색어 저장할 변수이다. searchBy가 id이면 아이디기준으로 검색하고,
    //createdBy일 경우 상품 등록자 아이디 기준으로 검색한다.
    private String searchBy;
    private String searchQuery = "";
}

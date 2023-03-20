package com.yuri.shoppingsite.domain.shop;

import com.yuri.shoppingsite.constant.Category;
import com.yuri.shoppingsite.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    //현재시간과 상품 등록일을 비교하여 상품 데이터를 조회한다.
    private String searchDateType;
    //상품 판매상태 기준으로 상품 데이터 조회
    private ItemSellStatus searchSellStatus;
    //상품 카테고리 기준으로 상품 데이터 조회
    private Category searchCategory;
    //상품 조회할 때 어떤 유형으로 조회할지 선택한다.
    private String searchBy;
    //조회할 검색어 저장할 변수이다. searchBy가 itemNm일 경우 상품명을 기준으로 검색하고,
    //createdBy일 경우 상품 등록자 아이디 기준으로 검색한다.
    private String searchQuery = "";
}

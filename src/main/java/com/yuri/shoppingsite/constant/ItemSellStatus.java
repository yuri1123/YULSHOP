package com.yuri.shoppingsite.constant;

// 상품이 품절인지 판매중인지 나타내는 enum 타입의 클래스
// 연관된 상수들을 모아둘 수 있으며, enum에 정의한 타입만 값을 가지도록
// 컴파일시 체크를 할수 있다는 장점이 있음

public enum ItemSellStatus {

    SELL, SOLD_OUT, NOT_SELL

}

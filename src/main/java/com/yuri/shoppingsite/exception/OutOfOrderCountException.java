package com.yuri.shoppingsite.exception;

public class OutOfOrderCountException extends RuntimeException{

    //상품의 주문 횟수가 음수일 때 발생시킬 exception 정의
    public OutOfOrderCountException(String message){
        super(message);
    }

}


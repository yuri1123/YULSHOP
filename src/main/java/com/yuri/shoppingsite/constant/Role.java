package com.yuri.shoppingsite.constant;

public enum Role {


    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    SILVER("ROLE_SILVER"),
    GOLD("ROLE_GOLD"),
    PLATINUM("ROLE_PLATINUM");

    Role(String value){
        this.value = value;
    }
    private String value;
}

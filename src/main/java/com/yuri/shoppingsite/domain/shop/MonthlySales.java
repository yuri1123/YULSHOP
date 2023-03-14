package com.yuri.shoppingsite.domain.shop;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "monthly_sales")
@NoArgsConstructor
public class MonthlySales {

    @Id
    private String date;
    private long sales;

    @Builder //빌더패턴 클래스 생성
    public MonthlySales(String date, long sales) {
        this.date = date;
        this.sales = sales;
    }
}
package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.shop.MonthlySales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonthlySalesRepository extends JpaRepository<MonthlySales, String> {

    //월별 매출 조회
    List<MonthlySales> findByDateStartsWith(String year);

}

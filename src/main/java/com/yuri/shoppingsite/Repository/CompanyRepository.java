package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query("select c from Company c")
    List<Company> findAllcompany();

    @Query("select c from Company c where c.companyname=:companyname")
    Company findbycompanyname(@Param("companyname") String companyname);

    @Query("select c from Company c where c.id=:id")
    Company findByid(Long id);

}

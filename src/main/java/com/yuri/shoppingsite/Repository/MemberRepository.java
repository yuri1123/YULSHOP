package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.constant.ItemSellStatus;
import com.yuri.shoppingsite.constant.Role;
import com.yuri.shoppingsite.domain.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
        //회원가입 시 중복 회원 있는지 검사하기 위해 이메일로 회원 검사
        Member findByEmail(String email);
        Member findByName(String name);

        @Modifying(clearAutomatically = true)
        @Query("update Member m set m.role=:role where m.id=:id")
        int updateUserRole(@Param(value="id") Long id, @Param(value="role") Role role);

//        @Modifying(clearAutomatically = true)
//        @Query("update Member m set m.accountbank=:accountbank, m.accountnum=:accountnum, m.accountname=:accountname where m.name=:name")
//        int updateAccount(@Param(value="name") Long name, @Param(value="accountbank") String accountbank,
//                          @Param(value="accountnum") String accountnum,@Param(value="accountname") String accountname);

}

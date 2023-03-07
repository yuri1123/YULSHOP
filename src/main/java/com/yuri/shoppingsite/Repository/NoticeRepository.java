package com.yuri.shoppingsite.Repository;

import com.yuri.shoppingsite.domain.community.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}

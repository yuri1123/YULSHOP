package com.yuri.shoppingsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //JPA의 Auditing 기능을 활성화한다.
public class AuditConfig {

    //등록자와 수정자를 처리해주는 AuditorAware을 빈으로 등록한다.
    @Bean
    public AuditorAware<String> auditorProvider(){
        return new AuditorAwareImpl();
    }


}

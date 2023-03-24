package com.yuri.shoppingsite.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //jpa auditing 활성화
public class JpaConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

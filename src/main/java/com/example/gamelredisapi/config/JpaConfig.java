package com.example.gamelredisapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.gamelredisapi.repository")
@EntityScan(basePackages = "com.example.gamelredisapi.entity")
public class JpaConfig {
    // 추가적인 JPA 설정이 필요하면 이곳에 작성합니다.
}
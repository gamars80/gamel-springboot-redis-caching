package com.example.gamelredisapi.component;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisHealthCheck implements CommandLineRunner {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        try {
            redisTemplate.opsForValue().set("test-key", "Redis 연결 성공!");
            String value = redisTemplate.opsForValue().get("test-key");
            System.out.println("Redis 테스트 값: " + value);
        } catch (Exception e) {
            System.err.println("Redis 연결 실패: " + e.getMessage());
        }
    }
}
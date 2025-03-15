package com.example.gamelredisapi.service;

import com.example.gamelredisapi.dto.PageDto;
import com.example.gamelredisapi.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final Duration TTL = Duration.ofSeconds(10);

    public String buildCacheKey(Long categoryId, int page, String sortDirection) {
        return String.format("product:category:%s:page:%d:sort:%s", categoryId, page, sortDirection.toLowerCase());
    }

    public void cacheProductPage(String key, PageDto<ProductDto> pageDto) {
        redisTemplate.opsForValue().set(key, pageDto, TTL);
    }

    public PageDto<ProductDto> getCachedPage(String key) {
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof PageDto) {
            return (PageDto<ProductDto>) cached;
        }
        return null;
    }

    public void evictCategoryCache(Long categoryId) {
        String pattern = "product:category:" + categoryId + ":page:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
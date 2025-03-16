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

    public void updateProductPriceInCache(Long categoryId, Long productId, int newPrice) {
        String pattern = "product:category:" + categoryId + ":page:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                Object cached = redisTemplate.opsForValue().get(key);
                if (cached instanceof PageDto) {
                    PageDto<ProductDto> pageDto = (PageDto<ProductDto>) cached;
                    boolean updated = false;
                    for (ProductDto dto : pageDto.getContent()) {
                        if (dto.getId().equals(productId)) {
                            dto.setPrice(newPrice);
                            updated = true;
                        }
                    }
                    // 캐시 내에 상품이 존재했다면 재설정
                    if (updated) {
                        redisTemplate.opsForValue().set(key, pageDto, TTL);
                    }
                }
            }
        }
    }

    // 상품 상세 캐시 키 생성
    public String buildProductDetailCacheKey(Long productId) {
        return String.format("product:detail:%d", productId);
    }

    // 캐시에서 상품 상세 조회 (캐시된 값이 ProductDto 형식이면 반환)
    public ProductDto getCachedProductDetail(String key) {
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof ProductDto) {
            return (ProductDto) cached;
        }
        return null;
    }

    // 상품 상세 정보를 캐시에 저장 (TTL에 약간의 랜덤 지터 적용)
    public void cacheProductDetail(String key, ProductDto productDto) {
        Duration ttlWithJitter = TTL.plusSeconds((long)(Math.random() * 5));
        redisTemplate.opsForValue().set(key, productDto, ttlWithJitter);
    }

    // 분산 락 획득 (setIfAbsent 사용)
    public boolean acquireLock(String lockKey, Duration timeout) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", timeout);
        return Boolean.TRUE.equals(success);
    }

    // 분산 락 해제
    public void releaseLock(String lockKey) {
        redisTemplate.delete(lockKey);
    }


}
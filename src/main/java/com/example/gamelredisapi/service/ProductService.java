package com.example.gamelredisapi.service;

import com.example.gamelredisapi.dto.PageDto;
import com.example.gamelredisapi.dto.ProductDto;
import com.example.gamelredisapi.entity.Product;
import com.example.gamelredisapi.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCacheService productCacheService;


    // 상품 목록 조회 – 페이지 번호별 캐싱 (페이지당 20개)
    @Cacheable(value = "products", key = "#pageable.pageNumber")
    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<ProductDto> getProductsByCategory(Long categoryId, int page, int size, String sortDirection) {
        String cacheKey = productCacheService.buildCacheKey(categoryId, page, sortDirection);

        // 캐싱된 DTO 조회
        PageDto<ProductDto> cachedDto = productCacheService.getCachedPage(cacheKey);
        if (cachedDto != null) {
            return convertToPage(cachedDto, page, size);
        }

        Sort sort = Sort.by("price");
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }

        Page<Product> productPage = productRepository.findByCategoryId(
                categoryId,
                PageRequest.of(page, size, sort)
        );

        Page<ProductDto> dtoPage = productPage.map(ProductDto::fromEntity);
        PageDto<ProductDto> pageDto = PageDto.fromPage(dtoPage);

        productCacheService.cacheProductPage(cacheKey, pageDto); // DTO 형태로 캐싱
        return dtoPage;
    }


    @Transactional
    public ProductDto updateProductPrice(Long productId, int newPrice) {
        // 1. 상품 조회 및 가격 업데이트
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
        product.setPrice(newPrice);
        productRepository.save(product);

        // 2. 캐시 데이터 업데이트 (Write-Through)
        productCacheService.updateProductPriceInCache(product.getCategoryId(), productId, newPrice);

        return ProductDto.fromEntity(product);
    }

    private Page<ProductDto> convertToPage(PageDto<ProductDto> dto, int page, int size) {
        return new org.springframework.data.domain.PageImpl<>(
                dto.getContent(),
                PageRequest.of(page, size),
                dto.getTotalElements()
        );
    }

}
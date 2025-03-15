package com.example.gamelredisapi.contoller;

import com.example.gamelredisapi.dto.ProductDto;
import com.example.gamelredisapi.entity.Product;
import com.example.gamelredisapi.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 리스트 API – 페이징 (페이지당 20개)
    @GetMapping
    public Page<Product> listProducts(@RequestParam(defaultValue = "0") int page) {
        PageRequest pageable = PageRequest.of(page, 20);
        return productService.getProducts(pageable);
    }

    // 상품 가격 수정 API
//    @PutMapping("/{id}/price")
//    public ResponseEntity<Product> updateProductPrice(@PathVariable Long id,
//                                                      @RequestBody PriceUpdateRequest request) {
//        Product updatedProduct = productService.updateProductPrice(id, request.getPrice());
//        return ResponseEntity.ok(updatedProduct);
//    }

    @GetMapping("/{categoryId}/products")
    public Page<ProductDto> getProducts(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort
    ) {
        return productService.getProductsByCategory(categoryId, page, size, sort);
    }
//
//    @PutMapping("/products/{id}")
//    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto) {
//        dto.setId(id); // DTO에 ID 바인딩
//        productService.updateProduct(dto);
//        return ResponseEntity.ok("상품 수정 및 캐시 삭제 완료");
//    }
}
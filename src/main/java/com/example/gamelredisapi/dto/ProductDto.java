package com.example.gamelredisapi.dto;

import com.example.gamelredisapi.entity.Product;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS, // 클래스명을 타입 정보로 사용
        include = JsonTypeInfo.As.PROPERTY, // JSON 속성으로 포함
        property = "@class" // 속성 이름 지정
)
public class ProductDto {
    private Long id;
    private String name;
    private int price;
    private Long categoryId;

    public static ProductDto fromEntity(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategoryId(product.getCategoryId());
        return dto;
    }
}
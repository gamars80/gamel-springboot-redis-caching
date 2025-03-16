package com.example.gamelredisapi.dto;

import com.example.gamelredisapi.entity.Product;
import lombok.Data;

@Data
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
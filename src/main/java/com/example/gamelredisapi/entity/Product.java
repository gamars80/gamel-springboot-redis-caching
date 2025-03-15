package com.example.gamelredisapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long categoryId;
    private String name;
    private String description;


    private int price;

    public static Product create(Long categoryId, String name, String description, int price) {
        Product product = new Product();
        product.categoryId = categoryId;
        product.name = name;
        product.description = description;
        product.price = price;
        return product;
    }

}
package com.example.gamelredisapi.dto;

import java.math.BigDecimal;

public class PriceUpdateRequest {
    private BigDecimal price;

    // Getter & Setter
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
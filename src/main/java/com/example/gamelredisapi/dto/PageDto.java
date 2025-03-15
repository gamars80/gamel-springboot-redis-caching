package com.example.gamelredisapi.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDto<T> {
    private List<T> content;  // 페이지 데이터 리스트
    private int pageNumber;
    private int pageSize;
    private long totalElements;

    public static <T> PageDto<T> fromPage(org.springframework.data.domain.Page<T> page) {
        return new PageDto<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
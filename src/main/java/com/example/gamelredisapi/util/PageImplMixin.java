package com.example.gamelredisapi.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class PageImplMixin<T> extends PageImpl<T> {

    @JsonCreator
    public PageImplMixin(@JsonProperty("content") List<T> content,
                         @JsonProperty("pageable") Pageable pageable,
                         @JsonProperty("total") long total) {
        super(content, pageable, total);
    }
}
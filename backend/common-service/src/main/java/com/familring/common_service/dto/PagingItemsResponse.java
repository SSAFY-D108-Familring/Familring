package com.familring.common_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
public class PagingItemsResponse<T> {
    int pageNo;
    int lastPageNo;
    @Builder.Default
    List<T> items = new ArrayList<>();
}

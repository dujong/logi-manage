package com.logi_manage.product_service.dto.request;

public record ProductFilterRequestDto(
        //상품명
        String name,
        //카테고리명
        String categoryName,
        //최소 가격 필터
        Long minPrice,
        //최대 가격 필터
        Long maxPrice,
        //정렬 기준
        String sortBy,
        //정렬 방향
        String sortDirection
) {
}

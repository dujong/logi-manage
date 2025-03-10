package com.logi_manage.order_fulfillment_service.dto.response;

public record ProductDetailResponseDto(
        //상품 id
        Long id,
        //상품명
        String name,
        //상품 가격
        Long price,
        //상품 설명
        String description,

        //상품 카테고리명
        String categoryName
) {
}

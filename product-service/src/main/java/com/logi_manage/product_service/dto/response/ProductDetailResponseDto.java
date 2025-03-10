package com.logi_manage.product_service.dto.response;

public record ProductDetailResponseDto(
        //상품 id
        long id,
        //상품명
        String name,
        //상품 가격
        long price,
        //상품 설명
        String description,

        //상품 카테고리명
        String categoryName

) {
}

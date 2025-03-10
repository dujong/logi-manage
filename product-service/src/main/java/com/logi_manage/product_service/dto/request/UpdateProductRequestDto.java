package com.logi_manage.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProductRequestDto(
        //상품명
        String name,
        //가격
        Long price,
        //상품 설명
        String description,
        //카테고리명
        String categoryName
) {
}

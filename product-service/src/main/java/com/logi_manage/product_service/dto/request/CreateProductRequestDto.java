package com.logi_manage.product_service.dto.request;

import com.logi_manage.product_service.entity.Category;
import jakarta.validation.constraints.NotBlank;

public record CreateProductRequestDto(
        //상품명
        @NotBlank
        String name,
        //가격
        long price,
        //상품 설명
        String description,
        //카테고리명
        String categoryName
) {
}

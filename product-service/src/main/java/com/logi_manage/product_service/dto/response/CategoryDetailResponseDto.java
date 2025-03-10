package com.logi_manage.product_service.dto.response;

public record CategoryDetailResponseDto(
        //카테고리 id
        long categoryId,
        //카테고리 명
        String name,
        //상위 카테고리
        Long parentCategoryId
) {
}

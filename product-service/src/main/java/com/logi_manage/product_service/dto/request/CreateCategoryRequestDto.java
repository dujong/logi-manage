package com.logi_manage.product_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequestDto(
        //카테고리명
        @NotBlank
        String name,
        //상위 카테고리 id
        Long parentId
) {
}

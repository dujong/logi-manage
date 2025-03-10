package com.logi_manage.product_service.dto.request;

public record UpdateCategoryRequestDto(
        //카테고리명
        String name,
        Long parentId
) {
}

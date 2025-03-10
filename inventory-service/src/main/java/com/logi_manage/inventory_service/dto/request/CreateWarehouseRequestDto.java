package com.logi_manage.inventory_service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateWarehouseRequestDto (
        //창고명
        @NotBlank
        String name,
        //우치
        @NotBlank
        String location
) {
}

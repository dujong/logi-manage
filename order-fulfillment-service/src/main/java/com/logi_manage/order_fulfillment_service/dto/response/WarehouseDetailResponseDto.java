package com.logi_manage.order_fulfillment_service.dto.response;

public record WarehouseDetailResponseDto(
        //창고 id
        Long id,
        //창고명
        String name,
        //창고 위치
        String location
) {
}

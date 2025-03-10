package com.logi_manage.inventory_service.dto.response;

public record InventoryDetailResponseDto(
        //재고 id
        Long id,
        //상품 id,
        Long productId,
        //재고 수량
        int quantity,
        //창고 id
        Long warehouseId

) {
}

package com.logi_manage.inventory_service.dto.request;

public record CreateInventoryRequestDto(
        //창고 id,
        Long warehouseId,
        //상품 id
        Long productId,
        //재고 수량
        int quantity
) {
}

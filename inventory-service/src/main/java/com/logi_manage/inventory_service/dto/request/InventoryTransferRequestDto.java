package com.logi_manage.inventory_service.dto.request;

public record InventoryTransferRequestDto(
        //출발 창고 id
        Long originWarehouseId,
        //도착 창고 id
        Long destinationWarehouseId,
        //상품 id
        Long productId,
        //이동 수량
        int quantity
) {
}

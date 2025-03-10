package com.logi_manage.inventory_service.dto.request;

public record InventoryFilterRequestDto(
        //창고 id
        Long warehouseId,
        //상품 id
        Long productId,
        //정렬 기준
        String sortBy,
        //정렬 방향
        String sortDirection
) {
}

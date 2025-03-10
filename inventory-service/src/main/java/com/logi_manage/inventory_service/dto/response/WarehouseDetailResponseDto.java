package com.logi_manage.inventory_service.dto.response;

import com.logi_manage.inventory_service.entity.Inventory;

import java.util.List;

public record WarehouseDetailResponseDto(
        //창고 id
        Long id,
        //창고명
        String name,
        //창고 위치
        String location,
        //재고 리스트
        List<Inventory> inventoryList
) {
}

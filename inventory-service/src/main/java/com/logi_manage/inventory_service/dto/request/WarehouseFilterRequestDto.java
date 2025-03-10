package com.logi_manage.inventory_service.dto.request;

public record WarehouseFilterRequestDto(
        //창고명
        String name,
        //위치명
        String location,
        //정렬 기준
        String sortBy,
        //정렬 방향
        String sortDirection
) {
}

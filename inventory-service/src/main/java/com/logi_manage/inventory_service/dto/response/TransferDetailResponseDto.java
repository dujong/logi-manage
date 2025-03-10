package com.logi_manage.inventory_service.dto.response;

import java.time.LocalDateTime;

public record TransferDetailResponseDto(
        //재고이동 id
        Long id,
        //상품 id,
        Long productId,
        //상품 이름
        String productName,
        //출발 창고 id
        Long originWarehouseId,
        //출발 창고 이름
        String originWarehouseName,
        //도착 창고 id
        Long destinationWarehouseId,
        //도착 창고 이름
        String destinationWarehouseName,
        //이동 수량
        int quantity,
        //처음 시점
        LocalDateTime createAt,
        //최근 업데이트 시점
        LocalDateTime modifiedAt
) {
}

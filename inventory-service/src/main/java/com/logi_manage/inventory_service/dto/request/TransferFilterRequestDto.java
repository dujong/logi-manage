package com.logi_manage.inventory_service.dto.request;

import java.time.LocalDateTime;

public record TransferFilterRequestDto(
        //상품 id
        Long productId,
        //출고된 창고 id
        Long fromWarehouseId,
        //입고된 창고 id,
        Long ToWarehouseId,
        //날짜 이후
        LocalDateTime dateFrom,
        //날짜 이전
        LocalDateTime dateTo
) {
}

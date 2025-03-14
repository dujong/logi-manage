package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.StockInStatus;

import java.time.LocalDateTime;

public record StockInFilterRequestDto(
        //입고 상태
        StockInStatus status,
        //창고 id
        Long warehouseId,
        //상품 id
        Long productId,
        //발주 id
        Long orderId,
        //날짜 이후
        LocalDateTime dateFrom,
        //날짜 이전
        LocalDateTime dateTo,
        //정렬 기준
        String sortBy,
        //정렬 방향
        String sortDirection
) {
}

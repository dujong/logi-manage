package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.StockInStatus;

public record StockInVerifyRequestDto(
        //입고 id
        Long id,
        //입고 상태
        StockInStatus status,
        //검수 의견
        String remarks
) {
}

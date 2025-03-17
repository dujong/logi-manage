package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.StockInStatus;

public record UpdateStockInRequestDto(
        //재고 id
        Long id,
        //입고 수량
        int quantity
) {
}

package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus;

public record StockInRequestDto(
        //주문 id
        Long orderId,
        //상품 id
        Long productId,
        //창고 id
        Long warehouseId,
        //상품 수량
        int quantity
) {
}

package com.logi_manage.order_fulfillment_service.dto.request;

public record CreateOrderFulfillmentRequestDto(
        //주문 id
        Long orderId,
        //창고 id
        Long warehouseId,
        //물건 id
        Long productId,
        //주문 수량
        int quantity
) {
}

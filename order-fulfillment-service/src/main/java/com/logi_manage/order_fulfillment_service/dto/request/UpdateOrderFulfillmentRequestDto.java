package com.logi_manage.order_fulfillment_service.dto.request;

public record UpdateOrderFulfillmentRequestDto(
        //재고 id
        Long id,
        //재고 수량
        int quantity
) {
}

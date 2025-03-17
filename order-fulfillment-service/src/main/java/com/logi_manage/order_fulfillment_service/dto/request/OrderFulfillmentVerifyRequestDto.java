package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus;

public record OrderFulfillmentVerifyRequestDto(
        //출고 id
        Long id,
        //출고 상태
        OrderFulfillmentStatus status,
        //검수 의견
        String remarks
) {
}

package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;

public record ReturnRefundVerifyRequestDto(
        //상태
        ReturnRefundStatus status,
        //검수 의견
        String remarks
) {
}

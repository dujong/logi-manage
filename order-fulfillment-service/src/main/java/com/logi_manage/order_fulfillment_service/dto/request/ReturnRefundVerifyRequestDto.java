package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundReason;

public record ReturnRefundVerifyRequestDto(
        //반품, 환불 사유
        ReturnRefundReason returnRefundReason
) {
}

package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.RefundReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnReason;

public record ReturnRefundVerifyRequestDto(
        //반품 사유
        ReturnReason returnReason,
        //환불 사유
        RefundReason refundReason
) {
}

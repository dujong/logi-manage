package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;

public record UpdateReturnRefundRequestDto(
        //환불, 반품 사유
        ReturnRefundReason returnRefundReason,
        //처리 상태
        ReturnRefundStatus status,
        //사유
        String remarks
) {
}

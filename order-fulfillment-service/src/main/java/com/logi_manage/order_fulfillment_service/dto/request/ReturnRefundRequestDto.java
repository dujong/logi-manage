package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.RefundReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;

public record ReturnRefundRequestDto(
        //주문 id
        Long orderId,
        //상품 id
        Long productId,
        //고객 id
        Long customerId,
        //처리 상태
        ReturnRefundStatus status,
        //반품 사유
        ReturnReason returnReason,
        //환불 사유
        RefundReason refundReason
) {
}

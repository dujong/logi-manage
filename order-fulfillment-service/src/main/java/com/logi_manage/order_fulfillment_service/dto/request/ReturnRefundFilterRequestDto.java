package com.logi_manage.order_fulfillment_service.dto.request;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;

import java.time.LocalDateTime;

public record ReturnRefundFilterRequestDto(
        //반품, 환불 상태
        ReturnRefundStatus status,
        //반품, 환불 이유
        ReturnRefundReason reason,
        //고객 id
        Long customerId,
        //주문 id
        Long orderId,
        //주문 아이템 id,
        Long orderItemId,
        //상품 id
        Long productId,
        //창고 id
        Long warehouseId,
        //주문 날짜
        LocalDateTime dateFrom,
        LocalDateTime dateTo
) {
}

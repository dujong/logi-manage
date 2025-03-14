package com.logi_manage.order_fulfillment_service.dto.response;

import com.logi_manage.order_fulfillment_service.constant.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponseDto(
        //주문 id
        Long id,
        //고객 id
        Long customerId,
        //고객 이름
        String customerName,
        //주문 상태
        OrderStatus orderStatus,
        //주문 날짜
        LocalDateTime createdAt
) {
}

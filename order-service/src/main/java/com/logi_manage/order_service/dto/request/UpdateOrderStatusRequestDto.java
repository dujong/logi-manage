package com.logi_manage.order_service.dto.request;

import com.logi_manage.order_service.constant.OrderStatus;

public record UpdateOrderStatusRequestDto(
        //주문 상태
        OrderStatus orderStatus
) {
}

package com.logi_manage.order_service.dto.response;

import com.logi_manage.order_service.constant.ShippingStatus;

public record OrderItemsStatusResponseDto(
        //주문 아이템 id
        Long orderItemId,
        //주문 상태
        ShippingStatus status,
        //메세지
        String message
) {
}

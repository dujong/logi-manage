package com.logi_manage.order_service.dto.response;

import java.util.List;

public record CancelOrderResponseDto(
        //주문 id
        Long orderId,
        //주문 아이템 상태
        List<OrderItemsStatusResponseDto> orderItemsStatusResponseDtoList
) {
}

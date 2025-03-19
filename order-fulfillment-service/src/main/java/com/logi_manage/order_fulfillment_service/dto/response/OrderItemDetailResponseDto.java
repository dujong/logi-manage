package com.logi_manage.order_fulfillment_service.dto.response;

public record OrderItemDetailResponseDto(
        Long id,
        Long productId,
        int quantity,
        int price
) {
}

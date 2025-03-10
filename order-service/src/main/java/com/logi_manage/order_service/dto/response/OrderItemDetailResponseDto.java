package com.logi_manage.order_service.dto.response;

import com.logi_manage.order_service.entity.OrderItem;

import java.util.List;

public record OrderItemDetailResponseDto(
        Long id,
        Long productId,
        int quantity,
        int price
) {
    public OrderItemDetailResponseDto(OrderItem orderItem) {
        this(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getPrice());
    }

    public static List<OrderItemDetailResponseDto> mapEntityToDto(List<OrderItem> orderItemList) {
        return orderItemList.stream().map(orderItem -> new OrderItemDetailResponseDto(orderItem.getId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getPrice())).toList();
    }
}

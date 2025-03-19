package com.logi_manage.order_service.service;

import com.logi_manage.order_service.dto.response.OrderItemDetailResponseDto;

import java.util.List;

public interface OrderItemService {
    List<OrderItemDetailResponseDto> getOrderItemList(Long orderId);

    OrderItemDetailResponseDto getOrderItem(Long orderItemId);
}

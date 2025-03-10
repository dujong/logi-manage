package com.logi_manage.order_service.service;

import com.logi_manage.order_service.dto.request.CreateOrderRequestDto;
import com.logi_manage.order_service.dto.request.OrderFilterRequestDto;
import com.logi_manage.order_service.dto.request.UpdateOrderStatusRequestDto;
import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface OrderService {
    Long createOrder(CreateOrderRequestDto createOrderRequestDto);

    void updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    void deleteOrder(Long orderId);

    Page<OrderDetailResponseDto> getOrderList(OrderFilterRequestDto filterRequestDto, Pageable pageable);

    OrderDetailResponseDto getOrder(Long orderId);
}

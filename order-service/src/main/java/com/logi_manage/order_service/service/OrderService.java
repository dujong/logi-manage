package com.logi_manage.order_service.service;

import com.logi_manage.order_service.dto.request.CreateOrderRequestDto;
import com.logi_manage.order_service.dto.request.OrderFilterRequestDto;
import com.logi_manage.order_service.dto.request.UpdateOrderStatusRequestDto;
import com.logi_manage.order_service.dto.response.DeleteOrderResponseDto;
import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_service.dto.response.OrderItemsStatusResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    Long createOrder(CreateOrderRequestDto createOrderRequestDto);

    void updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    DeleteOrderResponseDto deleteOrder(Long orderId);

    Page<OrderDetailResponseDto> getOrderList(OrderFilterRequestDto filterRequestDto, Pageable pageable);

    OrderDetailResponseDto getOrder(Long orderId);
}

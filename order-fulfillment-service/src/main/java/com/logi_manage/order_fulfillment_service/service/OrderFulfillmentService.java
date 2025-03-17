package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.dto.request.CreateOrderFulfillmentRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.OrderFulfillmentFilterRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.OrderFulfillmentVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateOrderFulfillmentRequestDto;
import com.logi_manage.order_fulfillment_service.dto.response.OrderFulfillmentDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface OrderFulfillmentService {
    Long createOrderFulfillment(CreateOrderFulfillmentRequestDto orderFulfillmentRequestDto);

    ResponseEntity<String> verifyOrderFulfillment(OrderFulfillmentVerifyRequestDto verifyRequestDto);

    ResponseEntity<String> updateOrderFulfillmentQuantity(Long orderFulfillmentId, UpdateOrderFulfillmentRequestDto updateOrderFulfillmentRequestDto);

    Page<OrderFulfillmentDetailResponseDto> getOrderFulfillmentList(OrderFulfillmentFilterRequestDto filterRequestDto, Pageable pageable);

    OrderFulfillmentDetailResponseDto getOrderFulfillment(Long orderFulfillmentId, OrderFulfillmentFilterRequestDto filterRequestDto);
}

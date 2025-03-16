package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.dto.request.CreateOrderFulfillmentRequestDto;

public interface OrderFulfillmentService {
    void createOrderFulfillment(CreateOrderFulfillmentRequestDto orderFulfillmentRequestDto);
}

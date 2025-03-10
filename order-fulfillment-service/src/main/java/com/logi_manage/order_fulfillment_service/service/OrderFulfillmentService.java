package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.dto.request.StockInRequestDto;

public interface OrderFulfillmentService {
    void processStockIn(StockInRequestDto stockInRequestDto);
}

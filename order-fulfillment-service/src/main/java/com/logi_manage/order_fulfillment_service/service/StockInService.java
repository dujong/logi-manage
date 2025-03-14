package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.dto.request.StockInFilterRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.StockInRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.StockInVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateStockInRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface StockInService {
    ResponseEntity<String> processStockIn(StockInRequestDto stockInRequestDto);

    ResponseEntity<String> verifyStockIn(StockInVerifyRequestDto stockInVerifyRequestDto);

    ResponseEntity<String> updateStockInQuantity(Long stockId, UpdateStockInRequestDto updateStockInRequestDto);

    Page<?> getStockInList(StockInFilterRequestDto filterRequestDto, Pageable pageable);
}

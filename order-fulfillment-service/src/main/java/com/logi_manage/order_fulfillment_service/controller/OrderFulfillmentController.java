package com.logi_manage.order_fulfillment_service.controller;

import com.logi_manage.order_fulfillment_service.dto.request.StockInRequestDto;
import com.logi_manage.order_fulfillment_service.service.OrderFulfillmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/fulfillment")
public class OrderFulfillmentController {
    private final OrderFulfillmentService orderFulfillmentService;

    /**
     * list up
     * - [ ]  발주된 상품 입고 처리
     * - [ ]  입고 검수 및 승인
     * - [ ]  입고 기록 조회
     * - [ ]  주문 출고 처리
     * - [ ]  출고 검수 및 포장
     * - [ ]  출고 기록 조회
     * - [ ]  기간별 입출고 기록 조회
     * - [ ]  상품별 재고 변동 추이
     */

    @PostMapping("/stock-in")
    public ResponseEntity<?> processStockIn(@RequestBody StockInRequestDto stockInRequestDto) {
        orderFulfillmentService.processStockIn(stockInRequestDto);
        return null;
    }
}

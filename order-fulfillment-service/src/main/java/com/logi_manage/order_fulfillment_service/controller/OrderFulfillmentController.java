package com.logi_manage.order_fulfillment_service.controller;

import com.logi_manage.order_fulfillment_service.service.OrderFulfillmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/fulfillment")
public class OrderFulfillmentController {
    private final OrderFulfillmentService orderFulfillmentService;

//    주문 출고 처리	POST	/api/fulfillment	주문 출고 요청
//    출고 검수 및 포장	PUT	/api/fulfillment/verify	출고 검수 및 포장 처리
//    출고 기록 조회	GET	/api/fulfillment/history	출고 내역 조회

    /**
     * list up
     * - [O]  발주된 상품 입고 처리
     * - [O]  입고 검수 및 승인
     * - [ ]  입고 기록 조회
     * - [ ]  주문 출고 처리
     * - [ ]  출고 검수 및 포장
     * - [ ]  출고 기록 조회
     * - [ ]  기간별 입출고 기록 조회
     * - [ ]  상품별 재고 변동 추이
     */





}

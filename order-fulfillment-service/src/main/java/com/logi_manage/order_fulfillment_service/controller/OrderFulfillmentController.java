package com.logi_manage.order_fulfillment_service.controller;

import com.logi_manage.order_fulfillment_service.dto.request.*;
import com.logi_manage.order_fulfillment_service.dto.response.OrderFulfillmentDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.StockInDetailResponseDto;
import com.logi_manage.order_fulfillment_service.service.OrderFulfillmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
     * - [O]  주문 출고 처리
     * - [O]  출고 검수 및 확정
     * - [O]  출고 기록 조회
     */


    /**
     * 주문 출고 처리
     *
     * @param orderFulfillmentRequestDto 주문 출고 dto
     * @return 출고 id
     */
    @PostMapping
    public ResponseEntity<Long> createOrderFulfillment(@RequestBody CreateOrderFulfillmentRequestDto orderFulfillmentRequestDto) {
        Long orderFulfillmentId = orderFulfillmentService.createOrderFulfillment(orderFulfillmentRequestDto);
        return ResponseEntity.ok(orderFulfillmentId);
    }

    /**
     * 출고 검수
     *
     * @param verifyRequestDto 출고 검수 dto
     */
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOrderFulfillment(@RequestBody OrderFulfillmentVerifyRequestDto verifyRequestDto) {
        return orderFulfillmentService.verifyOrderFulfillment(verifyRequestDto);
    }

    /**
     * 출고 승인
     *
     * @param orderFulfillmentId               승인된 출고 id
     * @param updateOrderFulfillmentRequestDto 출고 update dto
     */
    @PostMapping("/{orderFulfillmentId}/update")
    public ResponseEntity<String> updateOrderFulfillmentQuantity(@PathVariable Long orderFulfillmentId,
                                                                 @RequestBody UpdateOrderFulfillmentRequestDto updateOrderFulfillmentRequestDto) {
        return orderFulfillmentService.updateOrderFulfillmentQuantity(orderFulfillmentId, updateOrderFulfillmentRequestDto);
    }

    /**
     * 출고 리스트 조회
     *
     * @param filterRequestDto 필터링 dto
     * @param pageable         페이징
     * @return 출고 리스트
     */
    @GetMapping("/history")
    public ResponseEntity<Page<OrderFulfillmentDetailResponseDto>> getOrderFulfillmentList(OrderFulfillmentFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<OrderFulfillmentDetailResponseDto> orderFulfillmentList = orderFulfillmentService.getOrderFulfillmentList(filterRequestDto, pageable);
        return ResponseEntity.ok(orderFulfillmentList);
    }

    /**
     * 출고 상세 조회
     * @param orderFulfillmentId 조회할 출고 id
     * @param filterRequestDto 필터링
     * @return 출고 상세 info
     */
    @GetMapping("/{orderFulfillmentId}")
    public ResponseEntity<OrderFulfillmentDetailResponseDto> getOrderFulfillment(Long orderFulfillmentId, OrderFulfillmentFilterRequestDto filterRequestDto) {
        OrderFulfillmentDetailResponseDto orderFulfillment = orderFulfillmentService.getOrderFulfillment(orderFulfillmentId, filterRequestDto);
        return ResponseEntity.ok(orderFulfillment);
    }
}

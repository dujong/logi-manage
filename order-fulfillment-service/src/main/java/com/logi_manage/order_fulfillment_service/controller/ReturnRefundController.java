package com.logi_manage.order_fulfillment_service.controller;

import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.service.ReturnRefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReturnRefundController {

    /**
     * list up
     * - [O]  반품 요청
     * - [ ]  반품 검수 및 상태 변경
     * - [O]  환불 요청
     * - [ ]  환불 상태 변경
     * - [ ]  반품된 상품의 재고 처리
     */
    private final ReturnRefundService returnRefundService;

    /**
     * 반품 요청
     * @param requestDto 반품 요청 dto
     * @return 요청된 반품 id
     */
    @PostMapping("/refunds")
    public ResponseEntity<Long> requestRefund(@RequestBody ReturnRefundRequestDto requestDto) {
        Long refundId = returnRefundService.createRefund(requestDto);
        return ResponseEntity.ok(refundId);
    }

    /**
     * 반품 검수
     * @param refundId 검수할 반품 id
     * @param verifyRequestDto 검수 dto
     */
    @PostMapping("/refunds/{refundId}/verify")
    public ResponseEntity<String> verifyRefund(@PathVariable Long refundId, @RequestBody ReturnRefundVerifyRequestDto verifyRequestDto) {
        return returnRefundService.verifyRefund(refundId, verifyRequestDto);
    }


    @PostMapping("/refunds/{refundId}/update")
    public ResponseEntity<String> updateRefund(@PathVariable Long refundId, @RequestBody UpdateReturnRefundRequestDto updateReturnRefundRequestDto) {
        return returnRefundService.updateRefund(refundId, updateReturnRefundRequestDto);
    }


    /**
     * 환불 요청
     * @param requestDto 환불 요청 dto
     * @return 요청된 환불 id
     */
    @PostMapping("/returns")
    public ResponseEntity<Long> requestReturn(@RequestBody ReturnRefundRequestDto requestDto) {
        Long returnId = returnRefundService.createReturn(requestDto);
        return ResponseEntity.ok(returnId);
    }
}

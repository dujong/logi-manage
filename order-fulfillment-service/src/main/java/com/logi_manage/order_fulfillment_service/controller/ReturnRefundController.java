package com.logi_manage.order_fulfillment_service.controller;

import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundFilterRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.dto.response.ReturnRefundDetailResponseDto;
import com.logi_manage.order_fulfillment_service.service.ReturnRefundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ReturnRefundController {

    /**
     * list up
     * - [O]  반품 요청
     * - [O]  반품 검수 및 상태 변경
     * - [O]  환불 요청
     * - [O]  환불 상태 변경
     * - [O]  반품/환불 내역 조회
     */
    private final ReturnRefundService returnRefundService;

    /**
     * 반품 요청
     *
     * @param requestDto 반품 요청 dto
     * @return 요청된 반품 id
     */
    @PostMapping("/refunds")
    public ResponseEntity<Long> requestRefund(@RequestBody ReturnRefundRequestDto requestDto) {
        Long refundId = returnRefundService.createRefund(requestDto);
        return ResponseEntity.ok(refundId);
    }

    /**
     * 반품, 환불 검수
     *
     * @param returnRefundId         검수할 반품, 환불 id
     * @param verifyRequestDto 검수 dto
     */
    @PostMapping("/return-refund/{refundId}/verify")
    public ResponseEntity<String> verifyRefund(@PathVariable Long returnRefundId, @RequestBody ReturnRefundVerifyRequestDto verifyRequestDto) {
        return returnRefundService.verifyReturnRefund(returnRefundId, verifyRequestDto);
    }

    /**
     * 반품 상태 업데이트
     *
     * @param refundId                     업데이트 반품 id
     * @param updateReturnRefundRequestDto 반품 dto
     */
    @PostMapping("/refunds/{refundId}/update")
    public ResponseEntity<String> updateRefund(@PathVariable Long refundId, @RequestBody UpdateReturnRefundRequestDto updateReturnRefundRequestDto) {
        return returnRefundService.updateRefund(refundId, updateReturnRefundRequestDto);
    }


    /**
     * 환불 요청
     *
     * @param requestDto 환불 요청 dto
     * @return 요청된 환불 id
     */
    @PostMapping("/returns")
    public ResponseEntity<Long> requestReturn(@RequestBody ReturnRefundRequestDto requestDto) {
        Long returnId = returnRefundService.createReturn(requestDto);
        return ResponseEntity.ok(returnId);
    }

    /**
     * 환불 상태 업데이트
     * @param returnId                     업데이트 환불 id
     * @param updateReturnRefundRequestDto 환불 dto
     */
    @PostMapping("/returns/{returnId}/update")
    public ResponseEntity<String> updateReturn(@PathVariable Long returnId, @RequestBody UpdateReturnRefundRequestDto updateReturnRefundRequestDto) {
        return returnRefundService.updateReturn(returnId, updateReturnRefundRequestDto);
    }

    /**
     * 반품/환불 내역 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 반품/환불 list
     */
    @GetMapping("/return-refund")
    public ResponseEntity<?> getReturnRefundList(ReturnRefundFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<ReturnRefundDetailResponseDto> returnRefundList = returnRefundService.getReturnRefundList(filterRequestDto, pageable);
        return ResponseEntity.ok(returnRefundList);
    }
}

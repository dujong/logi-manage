package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundFilterRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.dto.response.ReturnRefundDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ReturnRefundService {
    ResponseEntity<String> verifyReturnRefund(Long refundId, ReturnRefundVerifyRequestDto verifyRequestDto);

    Long createRefund(ReturnRefundRequestDto requestDto);

    ResponseEntity<String> updateRefund(Long refundId, UpdateReturnRefundRequestDto updateReturnRefundRequestDto);

    Long createReturn(ReturnRefundRequestDto requestDto);

    ResponseEntity<String> updateReturn(Long returnId, UpdateReturnRefundRequestDto updateReturnRefundRequestDto);

    Page<ReturnRefundDetailResponseDto> getReturnRefundList(ReturnRefundFilterRequestDto filterRequestDto, Pageable pageable);
}

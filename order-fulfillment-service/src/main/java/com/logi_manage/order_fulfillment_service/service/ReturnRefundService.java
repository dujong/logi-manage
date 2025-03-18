package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateReturnRefundRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReturnRefundService {
    Long createRefund(ReturnRefundRequestDto requestDto);

    ResponseEntity<String> verifyRefund(Long refundId, ReturnRefundVerifyRequestDto verifyRequestDto);

    ResponseEntity<String> updateRefund(Long refundId, UpdateReturnRefundRequestDto updateReturnRefundRequestDto);

    Long createReturn(ReturnRefundRequestDto requestDto);
}

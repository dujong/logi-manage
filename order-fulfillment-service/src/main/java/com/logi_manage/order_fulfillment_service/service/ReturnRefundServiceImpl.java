package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.ReturnRefundVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateReturnRefundRequestDto;
import com.logi_manage.order_fulfillment_service.entity.ReturnRefund;
import com.logi_manage.order_fulfillment_service.repository.ReturnRefundRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus.PENDING;
import static com.logi_manage.order_fulfillment_service.constant.ReturnRefundType.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReturnRefundServiceImpl implements ReturnRefundService {
    private final ReturnRefundRepository returnRefundRepository;

    /**
     * 반품 요청
     * @param requestDto 반품 요청 dto
     * @return 요청된 반품 id
     */
    @Override
    public Long createRefund(ReturnRefundRequestDto requestDto) {
        ReturnRefund returnRefund = ReturnRefund.builder()
                .orderId(requestDto.orderId())
                .productId(requestDto.productId())
                .customerId(requestDto.customerId())
                .returnRefundType(REFUND)
                .refundReason(requestDto.refundReason())
                .status(requestDto.status())
                .build();
        ReturnRefund savedRefund = returnRefundRepository.save(returnRefund);
        return savedRefund.getId();
    }

    /**
     * 반품 검수
     * @param refundId 검수할 반품 id
     * @param verifyRequestDto 검수 dto
     */
    @Override
    public ResponseEntity<String> verifyRefund(Long refundId, ReturnRefundVerifyRequestDto verifyRequestDto) {
        ReturnRefund returnRefund = returnRefundRepository.findById(refundId).orElseThrow(() -> new EntityNotFoundException("Return not found"));

        if (returnRefund.getStatus() != PENDING) {
            return ResponseEntity.badRequest().body("Only PENDING return-refund can be verified");
        }

        returnRefund.setStatus(verifyRequestDto.status());
        returnRefund.setRemarks(verifyRequestDto.remarks());
        return ResponseEntity.ok("ReturnRefund verified successfully");
    }

    @Override
    public ResponseEntity<String> updateRefund(Long refundId, UpdateReturnRefundRequestDto updateReturnRefundRequestDto) {
        ReturnRefund returnRefund = returnRefundRepository.findById(refundId).orElseThrow(() -> new EntityNotFoundException("Return not found"));
        //반품 사유 update
        returnRefund.setRefundReason(updateReturnRefundRequestDto.refundReason());

        //반품 사유에 따른 비지니스 로직 실행
        switch (updateReturnRefundRequestDto.refundReason()) {
            case ORDER_CANCELED -> {
                
            }

            case ITEM_NOT_DELIVERED -> {

            }


        }
        return null;
    }

    /**
     * 환불 요청
     * @param requestDto 환불 요청 dto
     * @return 요청된 환불 id
     */
    @Override
    public Long createReturn(ReturnRefundRequestDto requestDto) {
        ReturnRefund returnRefund = ReturnRefund.builder()
                .orderId(requestDto.orderId())
                .productId(requestDto.productId())
                .customerId(requestDto.customerId())
                .returnRefundType(RETURN)
                .returnReason(requestDto.returnReason())
                .status(requestDto.status())
                .build();
        ReturnRefund savedReturn = returnRefundRepository.save(returnRefund);
        return savedReturn.getId();
    }
}

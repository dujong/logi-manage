package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.constant.RefundReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;
import com.logi_manage.order_fulfillment_service.constant.ShippingStatus;
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
import org.springframework.web.client.RestTemplate;

import static com.logi_manage.order_fulfillment_service.constant.RefundReason.*;
import static com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus.PENDING;
import static com.logi_manage.order_fulfillment_service.constant.ReturnRefundType.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReturnRefundServiceImpl implements ReturnRefundService {
    private final ReturnRefundRepository returnRefundRepository;

    private final RestTemplate restTemplate;

    /**
     * 반품 요청
     * @param requestDto 반품 요청 dto
     * @return 요청된 반품 id
     */
    @Override
    public Long createRefund(ReturnRefundRequestDto requestDto) {
        ReturnRefund returnRefund = ReturnRefund.builder()
                .orderId(requestDto.orderId())
                .orderItemId(requestDto.orderItemId())
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
        returnRefund.setStatus(ReturnRefundStatus.IN_PROGRESS);

        //반품의 처리 상태 체크
        if (returnRefund.getStatus() != PENDING) {
            return ResponseEntity.badRequest().body("Only PENDING return-refund can be verified");
        }

        //반품 사유에 따른 비지니스 로직 실행
        if (verifyRequestDto.refundReason() == ITEM_NOT_DELIVERED) {
            ShippingStatus shippingStatus = restTemplate.getForObject("localhost:8087/shipments/" + returnRefund.getOrderItemId() + "/status", ShippingStatus.class);

            if (shippingStatus == ShippingStatus.DELIVERED) {
                returnRefund.setStatus(ReturnRefundStatus.CANCELED);
                return ResponseEntity.badRequest().body("Item cannot be refunded because it has already been delivered.");
            }
            //TODO: DAMAGED_ITEM, PAYMENT_ERROR, CHANGE_OF_MIND, OTHER에 대한 자세한 비지니스 로직은 1차 MVP에서 구현 X
        } else if (verifyRequestDto.refundReason() == PAYMENT_ERROR || verifyRequestDto.refundReason() == CHANGE_OF_MIND) {
            //TODO: 시나리오
            // 창고의 재고를 increase한다

            //orderItemId로 정보 가져오기
            restTemplate.getForObject("localhost:8087/orders/")

            restTemplate.patchForObject("localhost:8084/inventories/" + returnRefund.)
        }

        return ResponseEntity.ok("ReturnRefund verified successfully");
    }

    /**
     * 반품 상태 업데이트
     * @param refundId 업데이트 반품 id
     * @param updateReturnRefundRequestDto 반품 dto
     */
    @Override
    public ResponseEntity<String> updateRefund(Long refundId, UpdateReturnRefundRequestDto updateReturnRefundRequestDto) {
        ReturnRefund returnRefund = returnRefundRepository.findById(refundId).orElseThrow(() -> new EntityNotFoundException("Return not found"));

        //반품 update
        returnRefund.setRefundReason(updateReturnRefundRequestDto.refundReason());
        returnRefund.setStatus(updateReturnRefundRequestDto.status());
        returnRefund.setRemarks(updateReturnRefundRequestDto.remarks());

        return ResponseEntity.ok("Refund request updated successfully.");
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

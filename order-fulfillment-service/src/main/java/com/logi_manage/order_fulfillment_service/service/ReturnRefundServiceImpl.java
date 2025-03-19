package com.logi_manage.order_fulfillment_service.service;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;
import com.logi_manage.order_fulfillment_service.constant.ShippingStatus;
import com.logi_manage.order_fulfillment_service.dto.request.*;
import com.logi_manage.order_fulfillment_service.dto.response.InventoryDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.OrderFulfillmentDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.OrderItemDetailResponseDto;
import com.logi_manage.order_fulfillment_service.dto.response.ReturnRefundDetailResponseDto;
import com.logi_manage.order_fulfillment_service.entity.ReturnRefund;
import com.logi_manage.order_fulfillment_service.repository.OrderFulfillmentRepository;
import com.logi_manage.order_fulfillment_service.repository.ReturnRefundRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.logi_manage.order_fulfillment_service.constant.ReturnRefundReason.*;
import static com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus.PENDING;
import static com.logi_manage.order_fulfillment_service.constant.ReturnRefundType.*;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ReturnRefundServiceImpl implements ReturnRefundService {
    private final ReturnRefundRepository returnRefundRepository;
    private final OrderFulfillmentRepository orderFulfillmentRepository;

    private final RestTemplate restTemplate;

    /**
     * 반품, 환불 검수
     * @param returnRefundId 검수할 반품, 환불 id
     * @param verifyRequestDto 검수 dto
     */
    @Override
    public ResponseEntity<String> verifyReturnRefund(Long returnRefundId, ReturnRefundVerifyRequestDto verifyRequestDto) {
        //TODO: DAMAGED_ITEM, PAYMENT_ERROR, CHANGE_OF_MIND, OTHER에 대한 자세한 비지니스 로직은 1차 MVP에서 구현 X
        ReturnRefund returnRefund = returnRefundRepository.findById(returnRefundId).orElseThrow(() -> new EntityNotFoundException("Return not found"));
        returnRefund.setStatus(ReturnRefundStatus.IN_PROGRESS);

        //반품, 환불의 처리 상태 체크
        if (returnRefund.getStatus() != PENDING) {
            return ResponseEntity.badRequest().body("Only PENDING return-refund can be verified");
        }

        //반품 사유에 따른 비지니스 로직 실행
        if (verifyRequestDto.returnRefundReason() == ITEM_NOT_DELIVERED) {
            ShippingStatus shippingStatus = restTemplate.getForObject("localhost:8087/shipments/" + returnRefund.getOrderItemId() + "/status", ShippingStatus.class);

            if (shippingStatus == ShippingStatus.DELIVERED) {
                returnRefund.setStatus(ReturnRefundStatus.CANCELED);
                return ResponseEntity.badRequest().body("Item cannot be refunded because it has already been delivered.");
            }

        } else if (verifyRequestDto.returnRefundReason() == PAYMENT_ERROR || verifyRequestDto.returnRefundReason() == CHANGE_OF_MIND) {
            //orderItemId로 quantity 정보 get
            OrderItemDetailResponseDto orderItemDto = restTemplate.getForObject("localhost:8087/order-items/" + returnRefund.getOrderItemId(), OrderItemDetailResponseDto.class);
            UpdateInventoryRequestDto updateInventoryRequestDto = new UpdateInventoryRequestDto(orderItemDto.quantity());

            //productId로 inventoryId 정보 get
            OrderFulfillmentDetailResponseDto orderFulfillmentDto = orderFulfillmentRepository.getOrderFulfillmentByOrderIdAndOrderItemIdAndProductId(returnRefund.getOrderId(), returnRefund.getOrderItemId(), returnRefund.getProductId());
            InventoryDetailResponseDto inventoryDto = restTemplate.getForObject("localhost:8084/inventories/" + orderFulfillmentDto.productId() + "/" + orderFulfillmentDto.warehouseId(), InventoryDetailResponseDto.class);

            //재고 increase
            restTemplate.patchForObject("localhost:8084/inventories/" + inventoryDto.id(), updateInventoryRequestDto, Void.class);
        }

        return ResponseEntity.ok("ReturnRefund verified successfully");
    }

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
                .returnRefundReason(requestDto.returnRefundReason())
                .status(requestDto.status())
                .build();
        ReturnRefund savedRefund = returnRefundRepository.save(returnRefund);
        return savedRefund.getId();
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
        returnRefund.setReturnRefundReason(updateReturnRefundRequestDto.returnRefundReason());
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
                .returnRefundReason(requestDto.returnRefundReason())
                .status(requestDto.status())
                .build();
        ReturnRefund savedReturn = returnRefundRepository.save(returnRefund);
        return savedReturn.getId();
    }

    /**
     * 환불 상태 업데이트
     * @param returnId                     업데이트 환불 id
     * @param updateReturnRefundRequestDto 환불 dto
     */
    @Override
    public ResponseEntity<String> updateReturn(Long returnId, UpdateReturnRefundRequestDto updateReturnRefundRequestDto) {
        ReturnRefund returnRefund = returnRefundRepository.findById(returnId).orElseThrow(() -> new EntityNotFoundException("Return not found"));

        //환불 update
        returnRefund.setReturnRefundReason(updateReturnRefundRequestDto.returnRefundReason());
        returnRefund.setStatus(updateReturnRefundRequestDto.status());
        returnRefund.setRemarks(updateReturnRefundRequestDto.remarks());

        return ResponseEntity.ok("Return request updated successfully.");
    }

    /**
     * 반품/환불 내역 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 반품/환불 list
     */
    @Override
    public Page<ReturnRefundDetailResponseDto> getReturnRefundList(ReturnRefundFilterRequestDto filterRequestDto, Pageable pageable) {
        return returnRefundRepository.findReturnRefundWithFilterAndSorting(
                filterRequestDto.status(),
                filterRequestDto.reason(),
                filterRequestDto.customerId(),
                filterRequestDto.orderId(),
                filterRequestDto.orderItemId(),
                filterRequestDto.productId(),
                filterRequestDto.warehouseId(),
                filterRequestDto.dateFrom(),
                filterRequestDto.dateTo(),
                pageable
        );
    }
}

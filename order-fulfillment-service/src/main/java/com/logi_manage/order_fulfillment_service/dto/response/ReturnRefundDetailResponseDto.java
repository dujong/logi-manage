package com.logi_manage.order_fulfillment_service.dto.response;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundReason;
import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;

import java.time.LocalDateTime;

public record ReturnRefundDetailResponseDto(
        //환불, 반품 id
        Long id,
        //환불, 반품 상태
        ReturnRefundStatus status,
        //환불, 반품 이유
        ReturnRefundReason reason,
        //고객 id
        Long customerId,
        //주문 id
        Long orderId,
        //주문 아이템 id
        Long orderItemId,
        //상품 id
        Long productId,
        //창고 id
        Long warehouseId,
        //상품명
        String productName,
        //상품 설명
        String productDescription,
        //창고명
        String warehouseName,
        //창고 위치
        String warehouseLocation,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
}

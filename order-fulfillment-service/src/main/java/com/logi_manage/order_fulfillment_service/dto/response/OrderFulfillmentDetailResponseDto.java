package com.logi_manage.order_fulfillment_service.dto.response;

import com.logi_manage.order_fulfillment_service.constant.StockInStatus;

import java.time.LocalDateTime;

public record OrderFulfillmentDetailResponseDto(
        //입고 기록 ID
        Long id,
        //발주 ID
        Long orderId,
        //상품 ID
        Long productId,
        //상품명 (추가적으로 필요)
        String productName,
        //입고 수량
        int quantity,
        //창고 ID
        Long warehouseId,
        //창고명 (추가적으로 필요)
        String warehouseName,
        //입고 상태
        StockInStatus status,
        //비고
        String remarks,
        //입고 등록일
        LocalDateTime createdAt,
        //최근 수정일
        LocalDateTime modifiedAt
) {
}

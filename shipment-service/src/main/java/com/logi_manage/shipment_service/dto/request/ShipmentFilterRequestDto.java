package com.logi_manage.shipment_service.dto.request;

import com.logi_manage.shipment_service.constant.ShippingStatus;

import java.time.LocalDateTime;

public record ShipmentFilterRequestDto(
        //주문 id
        Long orderId,
        //주문자 id
        Long customerId,
        //출고 id
        Long orderFulfillmentId,
        //배송 상태
        ShippingStatus status,
        //운송장 번호
        String trackingNumber,
        //택배사 이름
        String courierName,
        //배송 완료 날짜 이후
        LocalDateTime deliveredAfter,
        //배송 완료 날짜 이전
        LocalDateTime deliveredBefore,
        //배송 시작 날짜 이후
        LocalDateTime shippedAfter,
        //배송 시작 날짜 이전
        LocalDateTime shippedBefore,
        //정렬 기준
        String sortBy,
        //정렬 방향
        String sortDirection
) {
}

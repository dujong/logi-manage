package com.logi_manage.shipment_service.dto.request;

import com.logi_manage.shipment_service.constant.ShippingStatus;
import jakarta.validation.constraints.NotNull;

public record CreateShipmentRequestDto(
        @NotNull
        //주문 id
        Long orderId,
        @NotNull
        //주문자 id
        Long customerId,
        @NotNull
        //출고 id
        Long orderFulfillmentId,
        @NotNull
        //배송 상태
        ShippingStatus status,
        //운송장 번호
        String trackingNumber,
        //택배사 이름
        String courierName
) {
}

package com.logi_manage.shipment_service.dto.request;

import com.logi_manage.shipment_service.constant.ShippingStatus;

public record UpdateShipmentRequestDto(
        //주문 id
        Long orderId,
        //주문 아이템 id,
        Long orderItemId,
        //고객 id
        Long customerId,
        //출고 id
        Long orderFulfillmentId,
        //배송 상태
        ShippingStatus status,

        //운송장 번호
        String trackingNumber,
        //택배사 이름
        String courierName
) {
}

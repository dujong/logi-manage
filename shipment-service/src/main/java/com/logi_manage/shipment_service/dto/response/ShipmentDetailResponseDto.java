package com.logi_manage.shipment_service.dto.response;

import java.time.LocalDateTime;

public record ShipmentDetailResponseDto(
        //배송 id
        Long id,
        //주문 id
        Long orderId,
        //고객 id
        Long customerId,
        //고객명
        String customerName,

        //배송 등록일
        LocalDateTime createdAt,
        //최종 업데이트일
        LocalDateTime modifiedAt,
        //출고일
        LocalDateTime shippedDate,
        //배송 완료일
        LocalDateTime deliveredDate,
        //TODO: 반품 기능 구현 시 추가 예정
//        //반품 접수 날짜
//        LocalDateTime returnReceiveDate,
        //수령인 이름
        String receiverName,
        //수령인 연락처
        String receiverPhone,
        //수령인 주소
        String receiverAddress,
        //운송장 번호
        String trackingNumber,
        //택배사 이름
        String courierName
) {
}

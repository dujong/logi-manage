package com.logi_manage.order_service.dto.request;

import com.logi_manage.order_service.constant.OrderStatus;

public record UpdateOrderStatusRequestDto(
        //수령인 이름
        String receiverName,
        //수령인 연락처
        String receiverPhone,
        //수령인 주소
        String receiverAddress,
        //주문 상태
        OrderStatus status
) {
}

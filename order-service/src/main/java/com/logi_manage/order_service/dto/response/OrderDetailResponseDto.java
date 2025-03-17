package com.logi_manage.order_service.dto.response;

import com.logi_manage.order_service.constant.OrderStatus;
import com.logi_manage.order_service.entity.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponseDto(
        //주문 id
        Long id,
        //고객 id
        Long customerId,
        //고객 이름
        String customerName,
        //주문 상태
        OrderStatus status,
        //주문 아이템
        List<OrderItemDetailResponseDto> orderItemList,
        //주문 날짜
        LocalDateTime createdAt,
        //수령인 이름
        String receiverName,
        //수령인 연락처
        String receiverPhone,
        //수령인 주소
        String receiverAddress
) {
}

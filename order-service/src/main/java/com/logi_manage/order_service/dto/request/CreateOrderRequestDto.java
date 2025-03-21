package com.logi_manage.order_service.dto.request;

import com.logi_manage.order_service.constant.OrderStatus;
import com.logi_manage.order_service.entity.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequestDto(
        @NotNull
        //고객 id
        Long customerId,
        @NotNull
        //주문 상태
        OrderStatus orderStatus,
        @Valid
        //주문 항목
        List<CreateOrderItemRequestDto> createOrderItemRequestDtos,
        @NotNull
        //수령인 이름
        String receiverName,
        @NotNull
        //수령인 연락처
        String receiverPhone,
        @NotNull
        //수령인 주소
        String receiverAddress
) {
}

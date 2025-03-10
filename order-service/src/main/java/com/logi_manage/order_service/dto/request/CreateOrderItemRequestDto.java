package com.logi_manage.order_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemRequestDto(
        @NotNull
        //상품 id
        Long productId,
        @NotNull
        @Min(value = 1)
        //주문 수량
        int quantity,
        @NotNull
        @Min(value = 0)
        //상품 가격
        int price

) {
}

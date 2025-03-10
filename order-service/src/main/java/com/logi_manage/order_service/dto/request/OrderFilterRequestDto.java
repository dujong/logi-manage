package com.logi_manage.order_service.dto.request;

import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public record OrderFilterRequestDto(
        //주문 id
        Long orderId,
        //고객 id
        Long customerId,
        //상품 id
        Long productId,
        //날짜 이후
        LocalDateTime dateFrom,
        //날짜 이전
        LocalDateTime dateTo,
        //정렬 기준
        String sortBy,
        //정렬 방향
        String sortDirection
) {
}

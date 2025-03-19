package com.logi_manage.order_service.controller;

import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_service.dto.response.OrderItemDetailResponseDto;
import com.logi_manage.order_service.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    /**
     * list up
     * - [O] 주문 아이템 조회
     * - [O] 주문 아이템 리스트 조회
     */

    /**
     * 주문 아이템 리스트 조회
     * @param orderId 주문 id
     * @return 주문 아이템 list
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderItemDetailResponseDto>> getOrderItemList(@PathVariable Long orderId) {
        List<OrderItemDetailResponseDto> orderItemList = orderItemService.getOrderItemList(orderId);
        return ResponseEntity.ok(orderItemList);
    }

    /**
     * 주문 아이템 조회
     * @param orderItemId 조회할 주문 아이템 id
     * @return 주문 아이템 상세 info
     */
    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemDetailResponseDto> getOrderItem(@PathVariable Long orderItemId) {
        OrderItemDetailResponseDto orderItem = orderItemService.getOrderItem(orderItemId);
        return ResponseEntity.ok(orderItem);
    }
}

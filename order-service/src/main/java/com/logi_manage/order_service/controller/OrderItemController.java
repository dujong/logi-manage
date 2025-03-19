package com.logi_manage.order_service.controller;

import com.logi_manage.order_service.dto.response.OrderDetailResponseDto;
import com.logi_manage.order_service.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;

    @GetMapping("/{orderItemId}")
    public ResponseEntity<?> getOrderItem(@PathVariable Long orderItemId) {
         orderItemService.getOrderItem(orderItemId);

        return ResponseEntity.ok(null);
    }
}

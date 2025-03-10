package com.logi_manage.order_service.controller;

import com.logi_manage.order_service.dto.request.CreateCustomerRequestDto;
import com.logi_manage.order_service.dto.request.UpdateCustomerRequestDto;
import com.logi_manage.order_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    /**
     * list up
     * - [O]  고객 등록/수정/삭제
     */

    /**
     * 고객 신규 생성
     * @param createCustomerRequestDto 고객 생성 dto
     * @return 생성된 고객 id
     */
    @PostMapping
    public ResponseEntity<Long> createCustomer(@RequestBody CreateCustomerRequestDto createCustomerRequestDto) {
        Long customerId = customerService.createCustomer(createCustomerRequestDto);
        return ResponseEntity.ok(customerId);
    }

    /**
     * 고객 정보 수정
     * @param updateCustomerRequestDto 고객 수정 dto
     */
    @PatchMapping("/{customerId}")
    public ResponseEntity<Void> updateCustomer(@PathVariable Long customerId, @RequestBody UpdateCustomerRequestDto updateCustomerRequestDto) {
        customerService.updateCustomer(customerId, updateCustomerRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 고객 삭제
     * @param customerId 삭제할 고객 id
     */
    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> updateCustomer(@RequestBody @PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}

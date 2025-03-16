package com.logi_manage.order_fulfillment_service.controller;

import com.logi_manage.order_fulfillment_service.dto.request.StockInFilterRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.StockInRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.StockInVerifyRequestDto;
import com.logi_manage.order_fulfillment_service.dto.request.UpdateStockInRequestDto;
import com.logi_manage.order_fulfillment_service.dto.response.StockInDetailResponseDto;
import com.logi_manage.order_fulfillment_service.service.StockInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/stock-in")
public class StockInController {
    private final StockInService stockInService;

    /**
     * 발주된 상품 입고 처리
     * @param stockInRequestDto 입고 처리 dto
     */
    @PostMapping("/stock-in")
    public ResponseEntity<String> processStockIn(@RequestBody StockInRequestDto stockInRequestDto) {
        return stockInService.processStockIn(stockInRequestDto);
    }

    /**
     * 입고 검수
     * @param stockInVerifyRequestDto 입고 검수 dto
     */
    @PostMapping("/stock-in/verify")
    public ResponseEntity<String> verifyStockIn(@RequestBody StockInVerifyRequestDto stockInVerifyRequestDto) {
        return stockInService.verifyStockIn(stockInVerifyRequestDto);
    }

    /**
     * 입고 확정
     * @param stockId 확정된 입고 id
     * @param updateStockInRequestDto 입고 update dto
     */
    @PatchMapping("/{stockId}/update")
    public ResponseEntity<String> updateStockInQuantity(@PathVariable Long stockId,
                                                        @RequestBody UpdateStockInRequestDto updateStockInRequestDto) {
        return stockInService.updateStockInQuantity(stockId, updateStockInRequestDto);
    }

    /**
     * 입고 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 입고 리스트
     */
    @GetMapping("/history")
    public ResponseEntity<Page<StockInDetailResponseDto>> getOrderFulfillmentList(StockInFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<StockInDetailResponseDto> stockInList = stockInService.getStockInList(filterRequestDto, pageable);
        return ResponseEntity.ok(stockInList);
    }
}

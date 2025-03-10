package com.logi_manage.inventory_service.controller;

import com.logi_manage.inventory_service.dto.request.CreateWarehouseRequestDto;
import com.logi_manage.inventory_service.dto.request.UpdateWarehouseRequestDto;
import com.logi_manage.inventory_service.dto.request.WarehouseFilterRequestDto;
import com.logi_manage.inventory_service.dto.response.WarehouseDetailResponseDto;
import com.logi_manage.inventory_service.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    /**
     * list up
     * - [O]  신규 창고 등록
     * - [O]  창고 수정/삭제
     * - [O]  창고 목록 조회
     * - [O]  창고별 상품 재고 조회
     */

    private final WarehouseService warehouseService;

    /**
     * 창고 생성
     * @param warehouseRequestDto 등록 dto
     * @return 생성된 창고 id
     */
    @PostMapping
    public ResponseEntity<Long> createWarehouse(@RequestBody CreateWarehouseRequestDto warehouseRequestDto) {
        Long warehouseId = warehouseService.createWarehouse(warehouseRequestDto);
        return ResponseEntity.ok(warehouseId);
    }

    /**
     * 창고 수정
     * @param warehouseId 수정할 창고 id
     * @param warehouseRequestDto 수정 dto
     */
    @PatchMapping("/{warehouseId}")
    public ResponseEntity<Void> updateWarehouse(@PathVariable Long warehouseId, @RequestBody UpdateWarehouseRequestDto warehouseRequestDto) {
        warehouseService.updateWarehouse(warehouseId, warehouseRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 창고 삭제
     * @param warehouseId 삭제할 창고 id
     */
    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long warehouseId) {
        warehouseService.deleteWarehouse(warehouseId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 창고 목록 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 창고 list
     */
    @GetMapping
    public ResponseEntity<Page<WarehouseDetailResponseDto>> getWarehouseList(WarehouseFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<WarehouseDetailResponseDto> warehouseList = warehouseService.getWarehouseList(filterRequestDto, pageable);
        return ResponseEntity.ok(warehouseList);
    }

    /**
     * 창고&재고 조회
     * @param warehouseId 조회할 창고 id
     * @return 창고 상세 info
     */
    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDetailResponseDto> getWarehouse(@PathVariable Long warehouseId) {
        WarehouseDetailResponseDto warehouse = warehouseService.getWarehouse(warehouseId);
        return ResponseEntity.ok(warehouse);
    }

}

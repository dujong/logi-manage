package com.logi_manage.inventory_service.controller;

import com.logi_manage.inventory_service.dto.request.*;
import com.logi_manage.inventory_service.dto.response.InventoryDetailResponseDto;
import com.logi_manage.inventory_service.dto.response.TransferDetailResponseDto;
import com.logi_manage.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/inventories")
public class InventoryController {
    /**
     * list up
     * - [O]  재고 이동 (창고 간 이동)
     * - [O]  재고 이동 기록 조회
     */

    private InventoryService inventoryService;

    /**
     * 재고 등록
     * @param inventoryRequestDto 재고 등록 dto
     * @return 등록된 재고 id
     */
    @PostMapping
    public ResponseEntity<Long> createInventory(@RequestBody CreateInventoryRequestDto inventoryRequestDto) {
        Long inventoryId = inventoryService.createInventory(inventoryRequestDto);
        return ResponseEntity.ok(inventoryId);
    }

    /**
     * 재고 수정
     * @param inventoryId 수정할 재고 id
     * @param inventoryRequestDto 재고 수정 dto
     */
    @PatchMapping("/{inventoryId}")
    public ResponseEntity<Void> updateInventory(@PathVariable Long inventoryId, @RequestBody UpdateInventoryRequestDto inventoryRequestDto) {
        inventoryService.updateInventory(inventoryId, inventoryRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 재고 삭제
     * @param inventoryId 삭제할 재고 id
     */
    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> updateInventory(@PathVariable Long inventoryId) {
        inventoryService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 재고 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 재고 list
     */
    @GetMapping
    public ResponseEntity<Page<InventoryDetailResponseDto>> getInventoryList(InventoryFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<InventoryDetailResponseDto> inventoryList = inventoryService.getInventoryList(filterRequestDto, pageable);
        return ResponseEntity.ok(inventoryList);
    }

    /**
     * 재고 상세 조회
     * @param productId 상품 id
     * @param warehouseId 창고 id
     * @return 재고 상세 info
     */
    @GetMapping("/{productId}/{warehouseId}")
    public ResponseEntity<InventoryDetailResponseDto> getInventory(@PathVariable Long productId, Long warehouseId) {
        InventoryDetailResponseDto inventory = inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId);

        if (inventory == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(inventory);
    }

    /**
     * 재고 이동
     * @param transferRequestDto 재고 이동 dto
     */
    @PostMapping("/transfer")
    public ResponseEntity<Void> transferInventory(@RequestBody InventoryTransferRequestDto transferRequestDto) {
        inventoryService.transferInventory(transferRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 재고 이동 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 재고 이동 조회 list
     */
    @GetMapping("/transfers")
    public ResponseEntity<Page<TransferDetailResponseDto>> getInventoryTransfers(TransferFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<TransferDetailResponseDto> inventoryTransfers = inventoryService.getInventoryTransfers(filterRequestDto, pageable);
        return ResponseEntity.ok(inventoryTransfers);
    }

    /**
     * 재고 수량 증가
     * @param inventoryId 재고 id
     * @param inventoryRequestDto 재고 증가 dto
     */
    @PatchMapping("/{inventoryId}/increase")
    public ResponseEntity<Void> increaseInventory(@PathVariable Long inventoryId,
                                                  @RequestBody UpdateInventoryRequestDto inventoryRequestDto) {
        inventoryService.increaseInventory(inventoryId, inventoryRequestDto.quantity());
        return ResponseEntity.noContent().build();
    }

    /**
     * 재고 수량 감소
     * @param inventoryId 재고 id
     * @param inventoryRequestDto 재고 감소 dto
     */
    @PatchMapping("/{inventoryId}/decrease")
    public ResponseEntity<Void> decreaseInventory(@PathVariable Long inventoryId,
                                                  @RequestBody UpdateInventoryRequestDto inventoryRequestDto) {
        inventoryService.decreaseInventory(inventoryId, inventoryRequestDto.quantity());
        return ResponseEntity.noContent().build();
    }
}

package com.logi_manage.inventory_service.service;

import com.logi_manage.inventory_service.dto.request.*;
import com.logi_manage.inventory_service.dto.response.InventoryDetailResponseDto;
import com.logi_manage.inventory_service.dto.response.TransferDetailResponseDto;
import com.logi_manage.inventory_service.entity.Inventory;
import com.logi_manage.inventory_service.entity.InventoryTransfer;
import com.logi_manage.inventory_service.entity.Warehouse;
import com.logi_manage.inventory_service.repository.InventoryRepository;
import com.logi_manage.inventory_service.repository.InventoryTransferRepository;
import com.logi_manage.inventory_service.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService{
    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final InventoryTransferRepository transferRepository;

    /**
     * 재고 등록
     * @param inventoryRequestDto 재고 등록 dto
     * @return 등록된 재고 id
     */
    @Override
    public Long createInventory(CreateInventoryRequestDto inventoryRequestDto) {
        Warehouse warehouse = warehouseRepository.findById(inventoryRequestDto.warehouseId()).orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

        Inventory inventory = Inventory.builder()
                .productId(inventoryRequestDto.productId())
                .quantity(inventoryRequestDto.quantity())
                .warehouse(warehouse)
                .build();
        Inventory savedInventory = inventoryRepository.save(inventory);
        return savedInventory.getId();
    }

    /**
     * 재고 수정
     * @param inventoryId 수정할 재고 id
     * @param inventoryRequestDto 재고 수정 dto
     */
    @Override
    public void updateInventory(Long inventoryId, UpdateInventoryRequestDto inventoryRequestDto) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow(() -> new IllegalArgumentException("Inventory not found"));
        inventory.setQuantity(inventoryRequestDto.quantity());
    }

    /**
     * 재고 삭제
     * @param inventoryId 삭제할 재고 id
     */
    @Override
    public void deleteInventory(Long inventoryId) {
        inventoryRepository.deleteById(inventoryId);
    }

    /**
     * 재고 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 재고 list
     */
    @Override
    public Page<InventoryDetailResponseDto> getInventoryList(InventoryFilterRequestDto filterRequestDto, Pageable pageable) {
        Sort sort;
        if ("desc".equalsIgnoreCase(filterRequestDto.sortDirection())) {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        } else {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return inventoryRepository.findInventoryWithFilterAndSorting(filterRequestDto.warehouseId(), filterRequestDto.productId(), sortedPageable);
    }

    /**
     * 재고 상세 조회
     * @param productId 상품 id
     * @param warehouseId 창고 id
     * @return 재고 상세 info
     */
    @Override
    public InventoryDetailResponseDto getInventoryByProductAndWarehouse(Long productId, Long warehouseId) {
        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId).orElse(null);
        if (inventory == null) {
            return null;
        }

        return new InventoryDetailResponseDto(inventory.getId(), inventory.getProductId(), inventory.getQuantity(), Optional.ofNullable(inventory.getWarehouse()).map(Warehouse::getId).orElse(null));
    }

    /**
     * 재고 이동
     * @param transferRequestDto 재고 이동 dto
     */
    @Override
    public void transferInventory(InventoryTransferRequestDto transferRequestDto) {
        // 시나리오
        // 1. transfer 출발지 창고, 도착지 창고를 불러온다
        // 2. transfer 출발지 창고의 quantity를 검사해서 0이면 제거, 도착지 창고에 재고 채우기
        // 3. transfer entity 만들어서 기록 남기기

        Warehouse originWarehouse = warehouseRepository.findById(transferRequestDto.originWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("Origin Warehouse not found"));
        Warehouse destinationWarehouse = warehouseRepository.findById(transferRequestDto.destinationWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("Destination Warehouse not found"));

        //출발지 창고에서 해당 제품 찾기
        Inventory originInventory = originWarehouse.getInventoryList()
                .stream()
                .filter(inventory -> inventory.getProductId().equals(transferRequestDto.productId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Product not found in inventory"));

        int updatedOriginQuantity = originInventory.getQuantity() - transferRequestDto.quantity();
        //재고 검사
        if (updatedOriginQuantity < 0) {
            throw new IllegalArgumentException("Insufficient inventory quantity for transfer");
        }
        //재고 이동 검사
        if (updatedOriginQuantity == 0) {
            originWarehouse.getInventoryList().remove(originInventory);
        } else {
            originInventory.setQuantity(updatedOriginQuantity);
        }

        //도착지에서 재고 찾기
        Inventory destinationInventory = destinationWarehouse.getInventoryList()
                .stream()
                .filter(inventory -> inventory.getProductId().equals(transferRequestDto.productId()))
                .findFirst()
                .orElse(null);

        //재고가 없으면 신규 등록
        if (destinationInventory == null) {
            destinationInventory = Inventory.builder()
                    .productId(transferRequestDto.productId())
                    .quantity(transferRequestDto.quantity())
                    .warehouse(destinationWarehouse)
                    .build();
            destinationWarehouse.getInventoryList().add(destinationInventory);
        } else {
            destinationInventory.setQuantity(destinationInventory.getQuantity() + transferRequestDto.quantity());
        }

        //이동 기록 저장
        InventoryTransfer transfer = InventoryTransfer.builder()
                .productId(transferRequestDto.productId())
                .originWarehouse(originWarehouse)
                .destinationWarehouse(destinationWarehouse)
                .quantity(transferRequestDto.quantity())
                .build();
        transferRepository.save(transfer);
    }

    /**
     * 재고 이동 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 재고 이동 조회 list
     */
    @Override
    public Page<TransferDetailResponseDto> getInventoryTransfers(TransferFilterRequestDto filterRequestDto, Pageable pageable) {
        return transferRepository.findTransferWithFilters(
                filterRequestDto.productId(),
                filterRequestDto.fromWarehouseId(),
                filterRequestDto.ToWarehouseId(),
                filterRequestDto.dateFrom(),
                filterRequestDto.dateTo(),
                pageable);
    }

    /**
     * 재고 수량 증가
     * @param inventoryId 재고 id
     * @param quantity 재고 수량
     */
    @Override
    public void increaseInventory(Long inventoryId, int quantity) {
        inventoryRepository.increaseInventoryQuantity(inventoryId, quantity);
    }

    /**
     * 재고 수량 감소
     * @param inventoryId 재고 id
     * @param quantity 재고 수량
     */
    @Override
    public void decreaseInventory(Long inventoryId, int quantity) {
        inventoryRepository.decreaseInventory(inventoryId, quantity);
    }
}

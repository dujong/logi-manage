package com.logi_manage.inventory_service.service;

import com.logi_manage.inventory_service.dto.request.*;
import com.logi_manage.inventory_service.dto.response.InventoryDetailResponseDto;
import com.logi_manage.inventory_service.dto.response.TransferDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryService {
    Long createInventory(CreateInventoryRequestDto inventoryRequestDto);

    void updateInventory(Long inventoryId, UpdateInventoryRequestDto inventoryRequestDto);

    void deleteInventory(Long inventoryId);

    Page<InventoryDetailResponseDto> getInventoryList(InventoryFilterRequestDto filterRequestDto, Pageable pageable);
    void transferInventory(InventoryTransferRequestDto transferRequestDto);

    Page<TransferDetailResponseDto> getInventoryTransfers(TransferFilterRequestDto filterRequestDto, Pageable pageable);
}

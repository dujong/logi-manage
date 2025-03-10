package com.logi_manage.inventory_service.service;

import com.logi_manage.inventory_service.dto.request.CreateWarehouseRequestDto;
import com.logi_manage.inventory_service.dto.request.UpdateWarehouseRequestDto;
import com.logi_manage.inventory_service.dto.request.WarehouseFilterRequestDto;
import com.logi_manage.inventory_service.dto.response.WarehouseDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseService {
    Long createWarehouse(CreateWarehouseRequestDto warehouseRequestDto);

    void updateWarehouse(Long warehouseId, UpdateWarehouseRequestDto warehouseRequestDto);

    void deleteWarehouse(Long warehouseId);

    Page<WarehouseDetailResponseDto> getWarehouseList(WarehouseFilterRequestDto filterRequestDto, Pageable pageable);

    WarehouseDetailResponseDto getWarehouse(Long warehouseId);
}

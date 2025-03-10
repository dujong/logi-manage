package com.logi_manage.inventory_service.service;

import com.logi_manage.inventory_service.dto.request.CreateWarehouseRequestDto;
import com.logi_manage.inventory_service.dto.request.UpdateWarehouseRequestDto;
import com.logi_manage.inventory_service.dto.request.WarehouseFilterRequestDto;
import com.logi_manage.inventory_service.dto.response.WarehouseDetailResponseDto;
import com.logi_manage.inventory_service.entity.Warehouse;
import com.logi_manage.inventory_service.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    /**
     * 창고 생성
     * @param warehouseRequestDto 등록 dto
     * @return 생성된 창고 id
     */
    @Override
    public Long createWarehouse(CreateWarehouseRequestDto warehouseRequestDto) {
        Warehouse warehouse = Warehouse.builder()
                .name(warehouseRequestDto.name())
                .location(warehouseRequestDto.location())
                .build();

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return savedWarehouse.getId();
    }

    /**
     * 창고 수정
     * @param warehouseId 수정할 창고 id
     * @param warehouseRequestDto 수정 dto
     */
    @Override
    public void updateWarehouse(Long warehouseId, UpdateWarehouseRequestDto warehouseRequestDto) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        if (warehouseRequestDto.name() != null) {
            warehouse.setName(warehouseRequestDto.name());
        }
        if (warehouseRequestDto.location() != null) {
            warehouse.setLocation(warehouseRequestDto.location());
        }
    }

    /**
     * 창고 삭제
     * @param warehouseId 삭제할 창고 id
     */
    @Override
    public void deleteWarehouse(Long warehouseId) {
        warehouseRepository.deleteById(warehouseId);
    }

    /**
     * 창고 목록 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 창고 list
     */
    @Override
    public Page<WarehouseDetailResponseDto> getWarehouseList(WarehouseFilterRequestDto filterRequestDto, Pageable pageable) {
        Sort sort;
        if ("desc".equalsIgnoreCase(filterRequestDto.sortDirection())) {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        } else {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        }
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return warehouseRepository.findWarehousesWithFilterAndSorting(filterRequestDto.name(), filterRequestDto.location(), sortedPageable);
    }

    /**
     * 창고 조회
     * @param warehouseId 조회할 창고 id
     * @return 창고 상세 info
     */
    @Override
    public WarehouseDetailResponseDto getWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));
        return new WarehouseDetailResponseDto(warehouse.getId(), warehouse.getName(), warehouse.getLocation(), warehouse.getInventoryList());
    }
}

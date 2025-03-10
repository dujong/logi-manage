package com.logi_manage.inventory_service.repository;

import com.logi_manage.inventory_service.dto.response.WarehouseDetailResponseDto;
import com.logi_manage.inventory_service.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("SELECT new com.logi_manage.inventory_service.dto.response.WarehouseDetailResponseDto(w.id, w.name, w.location) " +
            "FROM Warehouse w " +
            "WHERE (:name IS NULL OR LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:location IS NULL OR LOWER(w.location) LIKE LOWER(CONCAT('%', :location, '%'))) ")
    Page<WarehouseDetailResponseDto> findWarehousesWithFilterAndSorting(
            @Param("name") String name,
            @Param("location") String location,
            Pageable pageable
    );
}

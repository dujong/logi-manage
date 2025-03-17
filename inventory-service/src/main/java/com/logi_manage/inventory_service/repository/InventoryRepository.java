package com.logi_manage.inventory_service.repository;

import com.logi_manage.inventory_service.dto.response.InventoryDetailResponseDto;
import com.logi_manage.inventory_service.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("SELECT new com.logi_manage.inventory_service.dto.response.InventoryDetailResponseDto(i.id, i.productId, i.quantity, i.warehouse.id) " +
            "FROM Inventory i " +
            "WHERE (:warehouseId IS NULL OR i.warehouse.id = :warehouseId) " +
            "AND (:productId IS NULL OR i.productId = :productId) ")
    Page<InventoryDetailResponseDto> findInventoryWithFilterAndSorting(
            @Param("warehouseId") Long warehouseId,
            @Param("productId") Long productId,
            Pageable pageable
    );

    Optional<Inventory> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity + :quantity WHERE i.id = :inventoryId")
    void increaseInventoryQuantity(Long inventoryId, int quantity);

    @Modifying
    @Query("UPDATE Inventory i SET i.quantity = i.quantity - :quantity WHERE i.id = :inventoryId AND i.quantity >= :quantity")
    void decreaseInventory(Long inventoryId, int quantity);
}

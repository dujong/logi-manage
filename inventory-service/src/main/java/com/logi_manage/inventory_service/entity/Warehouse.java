package com.logi_manage.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long id;

    //창고명
    @Column(nullable = false, length = 32)
    private String name;

    //위치
    @Column(length = 128)
    private String location;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    //재고 리스트
    @OneToMany(mappedBy = "warehouse", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inventory> inventoryList = new ArrayList<>();

    //current 창고 -> other 창고 재고
    @OneToMany(mappedBy = "originWarehouse", fetch = FetchType.LAZY)
    private List<InventoryTransfer> originTransfers;

    //other 창고 -> current 창고 재고
    @OneToMany(mappedBy = "destinationWarehouse", fetch = FetchType.LAZY)
    private List<InventoryTransfer> destinationTransfers;
}

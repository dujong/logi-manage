package com.logi_manage.order_fulfillment_service.entity;

import com.logi_manage.order_fulfillment_service.constant.FulfillmentType;
import com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus;
import com.logi_manage.order_fulfillment_service.constant.StockInStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(value = AuditingEntityListener.class)
public class StockIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_in_id")
    private Long id;

    //주문 id
    private Long orderId;

    //상품 id
    private Long productId;

    //입고 수량
    private int quantity;

    //창고 정보
    private Long warehouseId;

    //입고 상태
    @Enumerated(EnumType.STRING)
    private StockInStatus status;

    private String remarks;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}

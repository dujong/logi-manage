package com.logi_manage.order_fulfillment_service.entity;

import com.logi_manage.order_fulfillment_service.constant.OrderFulfillmentStatus;
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
public class OrderFulfillment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_fulfillment_id")
    private Long id;

    //상품 id
    private Long productId;

    //창고 id
    private Long warehouseId;

    //주문 id
    private Long orderId;

    //입출고 수량
    private int quantity;

    //처리 상태
    @Enumerated(value = EnumType.STRING)
    private OrderFulfillmentStatus status;

    private String remarks;

    //출고일
    private LocalDateTime shippedDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}

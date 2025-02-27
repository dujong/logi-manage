package com.logi_manage.order_fulfillment_service.entity;

import com.logi_manage.order_fulfillment_service.constant.ReturnRefundStatus;
import com.logi_manage.order_fulfillment_service.constant.ReturnRefundType;
import com.logi_manage.order_fulfillment_service.constant.ShippingStatus;
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
public class ReturnRefund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "return_refund_id")
    private Long id;

    //주문 id
    private Long orderId;

    //상품 id
    private Long productId;

    //유형
    @Enumerated(value = EnumType.STRING)
    private ReturnRefundType returnRefundType;

    //처리 상태
    @Enumerated(value = EnumType.STRING)
    private ReturnRefundStatus returnRefundStatus;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}

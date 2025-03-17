package com.logi_manage.shipment_service.entity;

import com.logi_manage.shipment_service.constant.ShippingStatus;
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
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private Long id;

    //주문 id
    private Long orderId;

    //주문자 id
    private Long customerId;

    //출고 id
    private Long orderFulfillmentId;

    //배송 상태
    @Enumerated(value = EnumType.STRING)
    private ShippingStatus status;

    //운송장 번호
    private String trackingNumber;
    //택배사 이름
    private String courierName;

    //배송 완료일
    private LocalDateTime deliveredDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;
}

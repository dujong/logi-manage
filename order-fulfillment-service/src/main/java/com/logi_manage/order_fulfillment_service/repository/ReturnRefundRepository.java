package com.logi_manage.order_fulfillment_service.repository;

import com.logi_manage.order_fulfillment_service.entity.ReturnRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnRefundRepository extends JpaRepository<ReturnRefund, Long> {
}

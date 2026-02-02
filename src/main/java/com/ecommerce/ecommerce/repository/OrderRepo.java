package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.dto.OrderSummaryDto;
import com.ecommerce.ecommerce.entity.OrderEntity;
import com.ecommerce.ecommerce.entity.OrderProduct;
import com.ecommerce.ecommerce.enums.OrderStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepo extends JpaRepository<OrderEntity,Long> {

    Page<OrderEntity> findByCustomer_User_Id(Long userId, Pageable pageable);

    @Query("""
    SELECT new com.ecommerce.ecommerce.dto.OrderSummaryDto(
        u.email,
        o.orderId,
        pe.productName,
        os.toStatus
    )
    FROM OrderProduct op
    JOIN op.order o
    JOIN op.product pv
    JOIN pv.product pe
    JOIN pe.seller se
    JOIN se.user u
    JOIN OrderStatusEntity os ON os.orderProduct = op
    WHERE os.id = (
        SELECT MAX(os2.id)
        FROM OrderStatusEntity os2
        WHERE os2.orderProduct = op
    )
    AND os.toStatus NOT IN :finalStatuses
""")
    List<OrderSummaryDto> findPendingOrdersForSeller(
        @Param("finalStatuses") List<OrderStatus> finalStatuses
    );




}

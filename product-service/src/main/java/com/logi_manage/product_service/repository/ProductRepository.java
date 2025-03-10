package com.logi_manage.product_service.repository;

import com.logi_manage.product_service.dto.response.ProductDetailResponseDto;
import com.logi_manage.product_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT new com.logi_manage.product_service.dto.response.ProductDetailResponseDto(p.id, p.name, p.price, p.description) " +
            "FROM Product p " +
            "JOIN p.category c " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:categoryName IS NULL OR c.name = :categoryName) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice) ")
    Page<ProductDetailResponseDto> findProductsWithFilterAndSorting(
            @Param("name") String name,
            @Param("categoryName") String categoryName,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable
    );
}

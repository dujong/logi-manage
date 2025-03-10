package com.logi_manage.product_service.service;

import com.logi_manage.product_service.dto.request.CreateProductRequestDto;
import com.logi_manage.product_service.dto.request.ProductFilterRequestDto;
import com.logi_manage.product_service.dto.request.UpdateProductRequestDto;
import com.logi_manage.product_service.dto.response.ProductDetailResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Long createProduct(CreateProductRequestDto productRequestDto);

    void updateProduct(Long productId, UpdateProductRequestDto productRequestDto);

    void deleteProduct(Long productId);

    ProductDetailResponseDto getProduct(Long productId);

    Page<ProductDetailResponseDto> getProductList(ProductFilterRequestDto filterRequestDto, Pageable pageable);
}

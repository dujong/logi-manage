package com.logi_manage.product_service.controller;

import com.logi_manage.product_service.dto.request.CreateProductRequestDto;
import com.logi_manage.product_service.dto.request.ProductFilterRequestDto;
import com.logi_manage.product_service.dto.request.UpdateProductRequestDto;
import com.logi_manage.product_service.dto.response.ProductDetailResponseDto;
import com.logi_manage.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    /**
     * list up
     * - [O]  상품 등록/수정/삭제
     * - [O]  상품 목록 및 개별 조회
     * - [O]  카테고리 관리
     */

    private final ProductService productService;

    /**
     * 상품 등록
     * @param productRequestDto 등록 dto
     * @return 등록된 상품 id
     */
    @PostMapping
    public ResponseEntity<Long> createProduct(@RequestBody CreateProductRequestDto productRequestDto) {
        Long productId = productService.createProduct(productRequestDto);
        return ResponseEntity.ok(productId);
    }

    /**
     * 상품 수정
     * @param productId 수정할 상품 id
     * @param productRequestDto 수정 dto
     */
    @PatchMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @RequestBody UpdateProductRequestDto productRequestDto) {
        productService.updateProduct(productId, productRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 삭제
     * @param productId 삭제할 상품 id
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 상품 상세조회
     * @param productId 조회할 상품 id
     * @return 상품 상세 info
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponseDto> getProduct(@PathVariable Long productId) {
        ProductDetailResponseDto product = productService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

    /**
     * 상품 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 상품 list
     */
    @GetMapping
    public ResponseEntity<?> getProductList(ProductFilterRequestDto filterRequestDto, Pageable pageable) {
        Page<ProductDetailResponseDto> productList = productService.getProductList(filterRequestDto, pageable);
        return ResponseEntity.ok(productList);
    }
}

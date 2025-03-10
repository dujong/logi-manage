package com.logi_manage.product_service.service;

import com.logi_manage.product_service.dto.request.CreateProductRequestDto;
import com.logi_manage.product_service.dto.request.ProductFilterRequestDto;
import com.logi_manage.product_service.dto.request.UpdateProductRequestDto;
import com.logi_manage.product_service.dto.response.ProductDetailResponseDto;
import com.logi_manage.product_service.entity.Category;
import com.logi_manage.product_service.entity.Product;
import com.logi_manage.product_service.repository.CategoryRepository;
import com.logi_manage.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    /**
     * 상품 등록
     * @param productRequestDto 등록 dto
     * @return 등록된 상품 id
     */
    @Override
    public Long createProduct(CreateProductRequestDto productRequestDto) {
        //카테고리 조회
        Category category = categoryRepository.findByName(productRequestDto.categoryName()).orElseThrow(() -> new IllegalArgumentException("The category name is incorrect"));

        //상품 객체 생성
        Product newProduct = Product.builder()
                .name(productRequestDto.name())
                .price(productRequestDto.price())
                .description(productRequestDto.description())
                .category(category)
                .build();

        //저장
        Product savedProduct = productRepository.save(newProduct);
        return savedProduct.getId();
    }

    /**
     * 상품 수정
     * @param productId 수정할 상품 id
     * @param productRequestDto 수정 dto
     */
    @Override
    public void updateProduct(Long productId, UpdateProductRequestDto productRequestDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        if (productRequestDto.name() != null) {
            product.setName(productRequestDto.name());
        }

        if (productRequestDto.price() != null) {
            product.setPrice(productRequestDto.price());
        }

        if (productRequestDto.description() != null) {
            product.setDescription(productRequestDto.description());
        }

        if (productRequestDto.categoryName() != null) {
            Category category = categoryRepository.findByName(productRequestDto.categoryName()).orElseThrow(() -> new IllegalArgumentException("The category name is incorrect"));
            product.setCategory(category);
        }
    }

    /**
     * 상품 삭제
     * @param productId 삭제할 상품 id
     */
    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    /**
     * 상품 상세조회
     * @param productId 조회할 상품 id
     * @return 상품 상세 info
     */
    @Override
    public ProductDetailResponseDto getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return new ProductDetailResponseDto(product.getId(), product.getName(), product.getPrice(), product.getDescription(), product.getCategory().getName());
    }

    /**
     * 상품 리스트 조회
     * @param filterRequestDto 필터링 dto
     * @param pageable 페이징
     * @return 상품 list
     */
    @Override
    public Page<ProductDetailResponseDto> getProductList(ProductFilterRequestDto filterRequestDto, Pageable pageable) {
        Sort sort;
        if ("desc".equalsIgnoreCase(filterRequestDto.sortDirection())) {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        } else {
            sort = Sort.by(Sort.Order.by(filterRequestDto.sortBy()));
        }

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return productRepository.findProductsWithFilterAndSorting(
                filterRequestDto.name(),
                filterRequestDto.categoryName(),
                filterRequestDto.minPrice(),
                filterRequestDto.maxPrice(),
                sortedPageable
        );
    }
}

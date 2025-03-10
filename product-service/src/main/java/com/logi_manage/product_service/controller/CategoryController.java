package com.logi_manage.product_service.controller;

import com.logi_manage.product_service.dto.request.CreateCategoryRequestDto;
import com.logi_manage.product_service.dto.request.UpdateCategoryRequestDto;
import com.logi_manage.product_service.dto.response.CategoryDetailResponseDto;
import com.logi_manage.product_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    /**
     * list up
     * - [O]  카테고리 추가/수정/삭제/조회
     */

    private final CategoryService categoryService;

    /**
     * 카테고리 추가
     *
     * @param categoryRequestDto 카테고리 추가 dto
     * @return 추가된 카테고리 id
     */
    @PostMapping
    public ResponseEntity<Long> createCategory(@RequestBody CreateCategoryRequestDto categoryRequestDto) {
        Long categoryId = categoryService.createCategory(categoryRequestDto);
        return ResponseEntity.ok(categoryId);
    }

    /**
     * 카테고리 수정
     *
     * @param categoryId         수정할 카테고리 id
     * @param categoryRequestDto 카테고리 수정 dto
     */
    @PatchMapping("/{categoryId}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @RequestBody UpdateCategoryRequestDto categoryRequestDto) {
        categoryService.updateCategory(categoryId, categoryRequestDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * 카테고리 삭제
     * @param categoryId 삭제할 카테고리 id
     */
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 카테고리 리스트 조회
     * @return 카테고리 리스트(flat 형태)
     */
    @GetMapping
    public ResponseEntity<List<CategoryDetailResponseDto>> getCategoryList() {
        List<CategoryDetailResponseDto> categoryList = categoryService.getCategoryList();
        return ResponseEntity.ok(categoryList);
    }
}

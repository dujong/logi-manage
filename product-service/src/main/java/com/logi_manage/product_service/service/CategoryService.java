package com.logi_manage.product_service.service;

import com.logi_manage.product_service.dto.request.CreateCategoryRequestDto;
import com.logi_manage.product_service.dto.request.UpdateCategoryRequestDto;
import com.logi_manage.product_service.dto.response.CategoryDetailResponseDto;

import java.util.List;

public interface CategoryService {
    Long createCategory(CreateCategoryRequestDto categoryRequestDto);

    void updateCategory(Long categoryId, UpdateCategoryRequestDto categoryRequestDto);

    void deleteCategory(Long categoryId);

    List<CategoryDetailResponseDto> getCategoryList();
}

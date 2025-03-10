package com.logi_manage.product_service.service;

import com.logi_manage.product_service.dto.request.CreateCategoryRequestDto;
import com.logi_manage.product_service.dto.request.UpdateCategoryRequestDto;
import com.logi_manage.product_service.dto.response.CategoryDetailResponseDto;
import com.logi_manage.product_service.entity.Category;
import com.logi_manage.product_service.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 추가
     * @param categoryRequestDto 카테고리 추가 dto
     * @return 추가된 카테고리 id
     */
    @Override
    public Long createCategory(CreateCategoryRequestDto categoryRequestDto) {
        Category parentCategory = null;
        //상위 카테고리 존재
        if (categoryRequestDto.parentId() != null) {
            parentCategory = categoryRepository.findById(categoryRequestDto.parentId()).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        }

        //새 카테고리 생성
        Category newCategory = Category.builder()
                .name(categoryRequestDto.name())
                .parent(parentCategory)
                .build();
        Category savedCategory = categoryRepository.save(newCategory);

        //부모 카테고리가 존재하면 자식 카테고리 리스트에 추가
        Optional.ofNullable(parentCategory).ifPresent(parent -> {
            if (parent.getChildren() == null) {
                parent.setChildren(new ArrayList<>());
            }
            parent.getChildren().add(savedCategory);
        });
        return newCategory.getId();
    }

    /**
     * 카테고리 수정
     * @param categoryId 수정할 카테고리 id
     * @param categoryRequestDto 카테고리 수정 dto
     */
    @Override
    public void updateCategory(Long categoryId, UpdateCategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        if (categoryRequestDto.name() != null) {
            category.setName(categoryRequestDto.name());
        }

        //카테고리 부모 수정
        if (categoryRequestDto.parentId() != null) {
            //origin parent에서 카테고리 삭제
            Optional.ofNullable(category.getParent())
                    .ifPresent(parent -> parent.getChildren().remove(category));

            //부모 카테고리 지정 및 자식 카테고리 추가
            Category parentCategory = categoryRepository.findById(categoryRequestDto.parentId()).orElseThrow(() -> new IllegalArgumentException("Category not found"));
            category.setParent(parentCategory);
            Optional.ofNullable(parentCategory)
                    .ifPresent(parent -> {
                        if (parent.getChildren() == null) {
                            parent.setChildren(new ArrayList<>());
                        }
                        parent.getChildren().add(category);
                    });
        }
    }

    /**
     * 카테고리 삭제
     * @param categoryId 삭제할 카테고리 id
     */
    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        //origin parent에서 카테고리 삭제
        Optional.ofNullable(category.getParent()).ifPresent(parent -> parent.getChildren().remove(category));
        //카테고리 삭제
        categoryRepository.delete(category);
    }

    /**
     * 카테고리 리스트 조회
     * @return 카테고리 리스트(flat 형태)
     */
    @Override
    public List<CategoryDetailResponseDto> getCategoryList() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .map(category -> new CategoryDetailResponseDto(
                        category.getId(),
                        category.getName(),
                        Optional.ofNullable(category.getParent()).map(Category::getId).orElse(null)
                )).collect(Collectors.toList());
    }
}

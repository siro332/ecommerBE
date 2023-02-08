package com.microservices.catalogservice.services;

import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.entities.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ICategoryService {
    List<CategoryDto> getAllCategories();
    Optional<CategoryDto> getCategoryByCode(String code);
    Set<Category> getListCategoryByCode(Set<String> categoryCodeList);
    Category addCategory(Category category);

    Optional<Category> update(String code, Category category);

    List<String> queryNameByIds(List<String> ids);

    void removeCategory(String code);
}

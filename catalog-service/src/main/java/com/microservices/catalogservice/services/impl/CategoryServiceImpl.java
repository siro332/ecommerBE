package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.repositories.CategoryRepository;
import com.microservices.catalogservice.services.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    private final MediaServiceImpl mediaService;
    private final CategoryRepository categoryRepository;
    private final DtoConverter dtoConverter;


    @Override
    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> allSubCategories = categoryRepository.findAllByParentCodeIsNotNullAndIsActiveIsTrue()
                .stream().map(dtoConverter::categoryEntityToDto)
                .collect(Collectors.toList());
        List<CategoryDto> rootCategories = categoryRepository.findAllByParentCodeIsNullAndIsActiveIsTrue()
                .stream().map(dtoConverter::categoryEntityToDto)
                .collect(Collectors.toList());
        Map<String, List<CategoryDto>> groupedSubCat = allSubCategories.stream()
                .collect(Collectors.groupingBy(CategoryDto::getParentCode));
        for (CategoryDto category : rootCategories
        ) {
            List<Media> mediaList = mediaService.getMediaByCode(category.getCode());
            findSubCat(category, groupedSubCat);
            if (mediaList.size() > 0)
                category.setImgUrl(mediaList.get(0).getImgUrl());
            category.setMediaList(mediaList);
        }
        return rootCategories;
    }

    @Override
    public Optional<CategoryDto> getCategoryByCode(String code) {
        Optional<Category> optionalCategory = categoryRepository.findByCodeAndIsActiveIsTrue(code);
        if (optionalCategory.isPresent()) {
            CategoryDto category = dtoConverter.categoryEntityToDto(optionalCategory.get());
            List<CategoryDto> allSubCategories = categoryRepository.findAllByParentCodeIsNotNullAndIsActiveIsTrue()
                    .stream().map(dtoConverter::categoryEntityToDto)
                    .collect(Collectors.toList());
            Map<String, List<CategoryDto>> groupedSubCat = allSubCategories.stream()
                    .collect(Collectors.groupingBy(CategoryDto::getParentCode));
            List<Media> mediaList = mediaService.getMediaByCode(category.getCode());

            CategoryDto resultCategory = findSubCat(category, groupedSubCat);
            if (mediaList.size() > 0)
                resultCategory.setImgUrl(mediaList.get(0).getImgUrl());
            resultCategory.setMediaList(mediaList);
            return Optional.of(resultCategory);
        }
        return Optional.empty();
    }

    @Override
    public Set<Category> getListCategoryByCode(Set<String> categoryCodeList) {
        return categoryRepository.findByCodeInAndIsActiveIsTrue(categoryCodeList);
    }

    public Set<Category> getListCategoryByChildCode(String categoryCode) {
        Set<Category> categories = new HashSet<>();
        do {
            Optional<Category> category = categoryRepository.findByCodeAndIsActiveIsTrue(categoryCode);
            if (category.isEmpty()){
                break;
            }
            categories.add(category.get());
            if (category.get().getParentCode() == null){
                break;
            }
            categoryCode = category.get().getParentCode();
        }while (true);
        return categories;
    }

    @Override
    public Category addCategory(Category category) {
        category.setCreatedAt(new Date());
        UUID uuid = UUID.randomUUID();
        category.setCode("CT" + Utils.uuidToBase64(uuid));
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> update(String code, Category newCategory) {
        Optional<Category> optionalCategory = categoryRepository.findByCodeAndIsActiveIsTrue(code);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(newCategory.getName());
            category.setIsActive(newCategory.getIsActive());
            category.setSlug(newCategory.getSlug());
            category.setParentCode(newCategory.getParentCode());
            category.setUpdatedAt(new Date());
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();
    }

    @Override
    public List<String> queryNameByIds(List<String> ids) {
        return categoryRepository.findAllByCodeInAndIsActiveIsTrue(ids).stream().map(Category::getName).collect(Collectors.toList());
    }

    @Override
    public void removeCategory(String code) {
        Optional<Category> optionalCategory = categoryRepository.findByCodeAndIsActiveIsTrue(code);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setIsActive(false);
            categoryRepository.save(category);
        }
    }

    private CategoryDto findSubCat(CategoryDto category, Map<String, List<CategoryDto>> result) {
        List<Media> mediaList = mediaService.getMediaByCode(category.getCode());
        if (mediaList.size() > 0)
            category.setImgUrl(mediaList.get(0).getImgUrl());
        category.setMediaList(mediaList);
        if (result.containsKey(category.getCode())) {
            List<CategoryDto> tempSubCats = result.get(category.getCode());
            List<CategoryDto> subCats = new ArrayList<>();
            for (CategoryDto subCat : tempSubCats
            ) {
                subCats.add(findSubCat(subCat, result));
            }
            category.setSubCategories(subCats);
        }
        return category;
    }
}

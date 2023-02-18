package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.models.entities.product_inventory.Brand;
import com.microservices.catalogservice.repositories.BrandRepository;
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
public class BrandService {

    private final BrandRepository brandRepository;

    public List<Brand> getAllBrand() {
        return brandRepository.findAllByIsActiveIsTrue();
    }

    public Optional<Brand> getBrandByCode(String code){
        return brandRepository.findByCodeAndIsActiveIsTrue(code);
    }

    public Optional<Brand> getBrandById(Long id){
        return brandRepository.findById(id);
    }

    public Optional<Brand> getBrandByName(String name){
        return brandRepository.findByNameAndIsActiveIsTrue(name);
    }

    public Brand addBrand(Brand brand){
        Optional<Brand> exitedBrand = brandRepository.findByNameAndIsActiveIsTrue(brand.getName());
        if (exitedBrand.isPresent()){
            return exitedBrand.get();
        }else {
            brand.setCreatedAt(new Date());
            UUID uuid = UUID.randomUUID();
            brand.setCode("BR" + Utils.uuidToBase64(uuid));
            return brandRepository.save(brand);
        }
    }

    public void remove(String code) {
        Optional<Brand> optionalBrand = brandRepository.findByCodeAndIsActiveIsTrue(code);
        if (optionalBrand.isPresent()) {
            Brand brand = optionalBrand.get();
            brand.setIsActive(false);
            brandRepository.save(brand);
        }
    }
}

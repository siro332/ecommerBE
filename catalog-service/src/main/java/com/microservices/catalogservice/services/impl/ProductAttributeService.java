package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.models.entities.product_inventory.ProductAttribute;
import com.microservices.catalogservice.repositories.ProductAttributeRepository;
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
public class ProductAttributeService {

    private final ProductAttributeRepository ProductAttributeRepository;

    public List<ProductAttribute> getAllProductAttribute() {
        return ProductAttributeRepository.findAll();
    }

    public Optional<ProductAttribute> getProductAttributeByCode(String code){
        return ProductAttributeRepository.findByCode(code);
    }

    public Optional<ProductAttribute> getProductAttributeByName(String name){
        return ProductAttributeRepository.findByName(name);
    }

    public ProductAttribute addProductAttribute(ProductAttribute ProductAttribute){
        Optional<ProductAttribute> exitedProductAttribute = ProductAttributeRepository.findByName(ProductAttribute.getName());
        if (exitedProductAttribute.isPresent()){
            return exitedProductAttribute.get();
        }else {
            UUID uuid = UUID.randomUUID();
            ProductAttribute.setCode("PA" + Utils.uuidToBase64(uuid));
            return ProductAttributeRepository.save(ProductAttribute);
        }
    }

}

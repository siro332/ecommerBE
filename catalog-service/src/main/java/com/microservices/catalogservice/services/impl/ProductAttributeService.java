package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.models.entities.product_inventory.ProductAttribute;
import com.microservices.catalogservice.repositories.ProductAttributeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    public List<ProductAttribute> getAllProductAttribute() {
        return productAttributeRepository.findAllByIsActiveIsTrue();
    }

    public Optional<ProductAttribute> getProductAttributeByCode(String code){
        return productAttributeRepository.findByCodeAndIsActiveIsTrue(code);
    }

    public Optional<ProductAttribute> getProductAttributeByName(String name){
        return productAttributeRepository.findByNameAndIsActiveIsTrue(name);
    }

    public ProductAttribute addProductAttribute(ProductAttribute productAttribute){
        Optional<ProductAttribute> exitedProductAttribute = productAttributeRepository.findByNameAndIsActiveIsTrue(productAttribute.getName());
        if (exitedProductAttribute.isPresent()){
            ProductAttribute newProductAtt = exitedProductAttribute.get();
            newProductAtt.setIsActive(true);
            return  productAttributeRepository.save(newProductAtt);
        }else {
            UUID uuid = UUID.randomUUID();
            productAttribute.setCode("PA" + Utils.uuidToBase64(uuid));
            return productAttributeRepository.save(productAttribute);
        }
    }
    public void remove(String code) {
        Optional<ProductAttribute> productAttributeOptional = productAttributeRepository.findByCodeAndIsActiveIsTrue(code);
        if (productAttributeOptional.isPresent()) {
            ProductAttribute productAttribute = productAttributeOptional.get();
            productAttribute.setIsActive(false);
            productAttributeRepository.save(productAttribute);
        }
    }

}

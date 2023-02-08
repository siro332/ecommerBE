package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.models.entities.Product;
import com.microservices.catalogservice.models.entities.product_inventory.Brand;
import com.microservices.catalogservice.models.entities.product_inventory.ProductAttribute;
import com.microservices.catalogservice.models.entities.product_inventory.ProductAttributeValue;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import com.microservices.catalogservice.models.pojo.ProductInventoryPojo;
import com.microservices.catalogservice.repositories.ProductAttributeRepository;
import com.microservices.catalogservice.repositories.ProductAttributeValueRepository;
import com.microservices.catalogservice.repositories.ProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final ProductAttributeValueRepository productAttributeValueRepository;


    public List<ProductInventory> getAllProductInventory() {
        return productInventoryRepository.findAll();
    }

    public List<ProductInventory> getByProductCode(String productCode){
        return productInventoryRepository.findByProductCode(productCode);
    }


    public ProductInventory addProductInventory(Long productId,String productCode, ProductInventoryPojo productInventoryPojo){
            UUID uuid = UUID.randomUUID();
            Set<ProductAttributeValue> productAttributeValues = new HashSet<>();
            for (var entry: productInventoryPojo.getAttributeValues().entrySet()
                 ) {
                ProductAttributeValue productAttributeValue = productAttributeValueRepository.save(ProductAttributeValue.builder()
                        .productAttribute(productAttributeRepository.findById(entry.getKey().longValue()).get())
                        .attributeValue(entry.getValue())
                        .code("PAB" + Utils.uuidToBase64(UUID.randomUUID()))
                        .build());
                productAttributeValues.add(productAttributeValue);
            }
            ProductInventory productInventory = ProductInventory.builder().productId(productId)
                    .retailPrice(Double.valueOf(productInventoryPojo.getRetailPrice()))
                    .sku(productId + Utils.uuidToBase64(uuid))
                    .productAttributeValues(productAttributeValues)
                    .units(productInventoryPojo.getUnits())
                    .isActive(true)
                    .productCode(productCode)
                    .unitSold(0l)
                    .build();
            return productInventoryRepository.save(productInventory);
    }

    public ProductInventory addProductInventory(ProductInventory productInventory){
                productInventory.setCreatedAt(new Date());
                UUID uuid = UUID.randomUUID();
                productInventory.setSku("PI" + Utils.uuidToBase64(uuid));
//                productInventory.setBrand(brand);
                return productInventoryRepository.save(productInventory);
    }

    public ProductInventory getBySku(String sku) {
        return productInventoryRepository.findBySku(sku).get();
    }

    public ProductInventory save(ProductInventory productInventory){
        return productInventoryRepository.save(productInventory);
    }
}

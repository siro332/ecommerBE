package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.models.entities.product_inventory.ProductAttribute;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import com.microservices.catalogservice.repositories.ProductAttributeRepository;
import com.microservices.catalogservice.repositories.ProductInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {
    private final ProductInventoryRepository productInventoryRepository;
    private final ProductAttributeRepository productAttributeRepository;
    @GetMapping("/product/{productCode}")
    public List<ProductInventory> findInventoryByProductCode(@PathVariable("productCode") String productCode) {
        log.info("Finding inventory for product code :" + productCode);
        List<ProductInventory> productInventoryList = productInventoryRepository.findByProductCode(productCode);
        if (productInventoryList.size() != 0) {
            return productInventoryList;
        } else {
            return Collections.emptyList();
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductInventory> findInventoryByCode(@PathVariable("code") String code) {
        log.info("Finding inventory for code :" + code);
        Optional<ProductInventory> productInventory = productInventoryRepository.findBySku(code);
        return productInventory.map(inventory -> new ResponseEntity<>(inventory, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));
    }
    @GetMapping("/product-attributes")
    public ResponseEntity<List<ProductAttribute>> findAllProductAttributes() {
        return new ResponseEntity<>(productAttributeRepository.findAll(), HttpStatus.OK);
    }
}

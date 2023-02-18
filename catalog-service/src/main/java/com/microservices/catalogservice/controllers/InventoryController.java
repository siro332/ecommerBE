package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.models.entities.product_inventory.ProductAttribute;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import com.microservices.catalogservice.repositories.ProductAttributeRepository;
import com.microservices.catalogservice.repositories.ProductInventoryRepository;
import com.microservices.catalogservice.services.impl.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final ProductAttributeService productAttributeService;
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
        return new ResponseEntity<>(productAttributeService.getAllProductAttribute(), HttpStatus.OK);
    }
    @PostMapping("/product-attributes/add")
    public ResponseEntity<?> addProductAttribute(@RequestBody ProductAttribute productAttribute){
        ProductAttribute newProductAttribute = productAttributeService.addProductAttribute(productAttribute);
        return ResponseEntity.ok().body(newProductAttribute);
    }
    @PostMapping("/product-attributes/delete/{code}")
    public ResponseEntity<?> removeBrand(@PathVariable String code){
        productAttributeService.remove(code);
        return ResponseEntity.ok().body("");
    }
}

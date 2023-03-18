package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.models.entities.WarrantyItem;
import com.microservices.catalogservice.services.impl.WarrantyItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/warranty")
@RequiredArgsConstructor
public class WarrantyItemController {
    private final WarrantyItemService warrantyItemService;

    @GetMapping("/search")
    public ResponseEntity<?> getWarrantyItemByProductSerial(@RequestParam(name = "product-serial") String productSerial){
        return ResponseEntity.ok(warrantyItemService.getByProductSerial(productSerial));
    }
    @GetMapping("/warranty-item/{id}")
    public ResponseEntity<?> getWarrantyItemById(@PathVariable Long id){
        return ResponseEntity.ok(warrantyItemService.getById(id));
    }
    @PostMapping("/create")
    public ResponseEntity<?> createWarrantyItem(@RequestBody WarrantyItem warrantyItem){
        try{
            return ResponseEntity.ok(warrantyItemService.addWarrantyItem(warrantyItem));
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating warranty-item: "+ e.getMessage());
        }
    }
    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteWarrantyItem(@PathVariable Long id){
        warrantyItemService.deleteItem(id);
        return ResponseEntity.ok("OK");
    }
}

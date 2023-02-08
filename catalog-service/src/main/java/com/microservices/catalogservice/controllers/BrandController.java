package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.models.entities.product_inventory.Brand;
import com.microservices.catalogservice.models.responses.UploadFileResponse;
import com.microservices.catalogservice.services.IMediaService;
import com.microservices.catalogservice.services.impl.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/catalog/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("")
    public List<Brand> getAllBrands(){
        return brandService.getAllBrand();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBrandById(@PathVariable Long id){
        Brand brand = brandService.getBrandById(id).get();
        return ResponseEntity.ok().body(brand);
    }
}

package com.microservices.catalogservice.controllers;

import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.exceptions.product.ProductNotFoundException;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Product;
import com.microservices.catalogservice.models.pojo.ProductPojo;
import com.microservices.catalogservice.models.specification.ProductSpecificationBuilder;
import com.microservices.catalogservice.services.impl.ProductServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.microservices.catalogservice.models.specification.SearchOperation.OR_PREDICATE_FLAG;

@RestController
@RequestMapping("/api/catalog/products")
@Slf4j
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;
    private final DtoConverter dtoConverter;

    @GetMapping("")
    public ResponseEntity<Map<String,Object>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "name") String sortParam,
                                                  @RequestParam(defaultValue = "0") int sortDirection) {
        try {
            Page<ProductDto> productPage;
            if (sortDirection == 0) {
                productPage = productService.getAllProducts(PageRequest.of(page, size, Sort.by(sortParam)));
            } else {
                productPage = productService.getAllProducts(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParam)));
            }
            Map<String, Object> response = getResponse(productPage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/product/{code}")
    public ProductDto productByCode(@PathVariable String code) {
        return productService.getProductDtoByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("Product with code ["+code+"] doesn't exist"));
    }
    @GetMapping("/category/{categoryCode}")
    public ResponseEntity<Map<String,Object>> getProductsByCategoryCode(@PathVariable String categoryCode,
                                                            @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "3") int size,
                                                             @RequestParam(defaultValue = "name") String sortParam,
                                                             @RequestParam(defaultValue = "0") int sortDirection) {
        try {
            Page<ProductDto> productPage;
            if (sortDirection == 0) {
                productPage = productService.getProductByCategoryCode(categoryCode,PageRequest.of(page, size, Sort.by(sortParam)));
            } else {
                productPage = productService.getProductByCategoryCode(categoryCode,PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParam)));
            }
            Map<String, Object> response = getResponse(productPage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createProduct(@RequestBody ProductPojo productPojo) {
        try{
            Product newProduct = productService.createProduct(productPojo);
            return ResponseEntity.ok().body(dtoConverter.productEntityToDto(newProduct));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error creating product: "+ e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProduct(@RequestBody ProductPojo productPojo) {
        try{
            Product newProduct = productService.updateProduct(productPojo);
            return ResponseEntity.ok().body(dtoConverter.productEntityToDto(newProduct));
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body("Error updating product: "+ e.getMessage());
        }
    }


    private Map<String, Object> getResponse(Page<ProductDto> productPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return response;
    }

    @GetMapping("/category/{categoryCode}/topSales")
    public ResponseEntity<Map<String,Object>> getProductsCodeByCategoryCode(@PathVariable String categoryCode,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "6") int size) {
        try {
            Page<ProductDto> productPage;
            productPage = productService.getProductCodeByCategoryCode(categoryCode, PageRequest.of(page, size));
            Map<String, Object> response = getResponse(productPage);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{code}")
    public ResponseEntity<?> deleteProduct(@PathVariable String code){
        try{
            productService.deleteProductByCode(code);
            return ResponseEntity.ok().body("");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error deleting product: "+ e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getPostsContains(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "DESC") String sortDirectionStr ,
            @RequestParam(defaultValue = "created_at") String sortParam,
            @RequestParam(defaultValue = "") String productName,
            @RequestParam(defaultValue = "") String categoryCode,
            @RequestParam(defaultValue = "0") long lowestPrice,
            @RequestParam(defaultValue = "99999999999") long highestPrice,
            @RequestParam(defaultValue = "") String brandName
            ) {
        try {
            Page<ProductDto> productPage;
            int sortDirection = sortDirectionStr.equals("DESC")? 1:0;
            if (sortDirection == 0) {
                productPage = productService.searchProduct(
                        "%" + productName + "%",
                        "%" + categoryCode + "%"
                        ,lowestPrice,
                        highestPrice,
                        "%" + brandName + "%",
                        PageRequest.of(page, size, Sort.by(sortParam)));
            } else {
                productPage = productService.searchProduct(
                        "%" + productName + "%",
                        "%" + categoryCode + "%"
                        ,lowestPrice,
                        highestPrice,
                        "%" + brandName + "%",
                        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortParam)));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("products", productPage.getContent());
            response.put("currentPage", productPage.getNumber());
            response.put("totalItems", productPage.getTotalElements());
            response.put("totalPages", productPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info(e.toString());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.microservices.catalogservice.services;

import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Product;
import com.microservices.catalogservice.models.pojo.ProductPojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface IProductService {
    Product createProduct(ProductPojo form);
    Product updateProduct(ProductPojo form);
    Page<ProductDto> getAllProducts(Pageable paging);
    Page<ProductDto> getProductContain(Pageable pageable, Specification<Product> spec);
    Optional<ProductDto> getProductDtoByCode(String code);
    void deleteProductByCode(String code);
    Page<ProductDto> getProductByCategoryCode(String userCode, Pageable paging);
}

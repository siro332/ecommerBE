package com.microservices.catalogservice.services.impl;

import com.microservices.catalogservice.common.Utils;
import com.microservices.catalogservice.converters.DtoConverter;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.models.entities.Product;
import com.microservices.catalogservice.models.entities.Review;
import com.microservices.catalogservice.models.entities.product_inventory.Brand;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import com.microservices.catalogservice.models.pojo.ProductInventoryPojo;
import com.microservices.catalogservice.models.pojo.ProductPojo;
import com.microservices.catalogservice.repositories.CategoryRepository;
import com.microservices.catalogservice.repositories.ProductRepository;
import com.microservices.catalogservice.services.IProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final ProductInventoryService productInventoryService;
    private final CategoryRepository categoryRepository;
    private final CategoryServiceImpl categoryService;
    private final MediaServiceImpl mediaService;
    private final DtoConverter dtoConverter;
    private final ReviewService reviewService;
    @Override
    public Product createProduct(ProductPojo form) {
        try {
            Set<Category> categories = categoryService.getListCategoryByChildCode(form.getCategoryCode());
            UUID uuid = UUID.randomUUID();
            Brand brand = brandService.getBrandById(Long.valueOf(form.getBrandId())).get();
            String productCode = "PD" + Utils.uuidToBase64(uuid);
            Product newProduct = Product.builder()
                    .name(form.getName())
                    .code(productCode)
                    .isActive(true)
                    .createdAt(new Date())
                    .description(form.getDescription())
                    .brand(brand)
                    .categories(categories)
                    .warrantyPeriod(form.getWarrantyPeriod()!=null? form.getWarrantyPeriod() : 1)
                    .build();
            newProduct = productRepository.save(newProduct);
            for (ProductInventoryPojo productInventoryPojo: form.getProductInventoryPojos()
                 ) {
                productInventoryService.addProductInventory(newProduct.getId(), productCode, productInventoryPojo);
            }
            return productRepository.findById(newProduct.getId()).get();
        } catch (Exception e) {
            log.error("Error while creating product");
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Product updateProduct(ProductPojo form) {
        try {
            Set<Category> categories = categoryService.getListCategoryByChildCode(form.getCategoryCode());
            Optional<Product> optionalProduct = productRepository.findByCodeAndIsActiveIsTrue(form.getCode());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setCategories(categories);
                product.setUpdatedAt(new Date());
                product.setIsActive(true);
                product.setIsActive(form.getIsActive());
                product.setName(form.getName());
                product.setDescription(form.getDescription());
                product.setWarrantyPeriod(form.getWarrantyPeriod()!=null? form.getWarrantyPeriod() : product.getWarrantyPeriod());
                productRepository.save(product);
                for (ProductInventoryPojo productInventoryPojo: form.getProductInventoryPojos()
                ) {
                    productInventoryService.update(productInventoryPojo);
                }
                return productRepository.findById(product.getId()).get();
            }else{
                log.error("Product with code: "+form.getCode()+" not found!");
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("Error while creating product");
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable paging) {
        Page<Product> productPage = productRepository.findByIsActiveIsTrue(paging);
        return productPage.map(this::getProductDetails);
    }

    @Override
    public Page<ProductDto> getProductContain(Pageable pageable, Specification<Product> spec) {
        Page<Product> productPage = productRepository.findAll(spec,pageable);
        return productPage.map(this::getProductDetails);
    }

    public Page<ProductDto> searchProduct(String productName, String categoryCode, long lowestPrice, long highestPrice, String brandName, Pageable pageable) {
        List<Product> productList = productRepository.search(productName,categoryCode,lowestPrice,highestPrice,brandName);
        List<ProductDto> productDtoList = productList.stream().map(this::getProductDetails).collect(Collectors.toList());
        Sort.Order order = pageable.getSort().stream().findFirst().get();
        switch (order.getProperty()){
            case "name":
                if (order.getDirection().isAscending()){
                    productDtoList.sort(Comparator.comparing(ProductDto::getName));
                }
                else {
                    productDtoList.sort(Comparator.comparing(ProductDto::getName).reversed());
                }
            case "price":
                if (order.getDirection().isAscending()){
                    productDtoList.sort(Comparator.comparing(ProductDto::getPrice));
                }
                else {
                    productDtoList.sort(Comparator.comparing(ProductDto::getPrice).reversed());
                }
        }
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), productList.size());
        Page<ProductDto> productPage = new PageImpl<>(productDtoList.subList(start, end), pageable, productDtoList.size());
        return productPage;
    }

    @Override
    public Optional<ProductDto> getProductDtoByCode(String code) {
       Optional<Product> optionalProduct = productRepository.findByCodeAndIsActiveIsTrue(code);
       if (optionalProduct.isPresent()) {
           log.info("Fetching media for product_code: " + code);
           Product product = optionalProduct.get();
           ProductDto productDto = getProductDetails(product);
           return Optional.of(productDto);
       }
       return Optional.empty();
    }

    public Optional<ProductDto> getProductDtoById(Long id) {
        Optional<Product> optionalProduct = productRepository.findByIdAndIsActiveIsTrue(id);
        if (optionalProduct.isPresent()) {
            log.info("Fetching media for product_id: " + id);
            Product product = optionalProduct.get();
            ProductDto productDto = getProductDetails(product);
            return Optional.of(productDto);
        }
        return Optional.empty();
    }

    public Optional<Product> getProductByCode(String code) {
        Optional<Product> optionalProduct = productRepository.findByCodeAndIsActiveIsTrue(code);
        if (optionalProduct.isPresent()) {
            return Optional.of(optionalProduct.get());
        }
        return Optional.empty();
    }

    @Override
    public void deleteProductByCode(String code) {
        try {
            Optional<Product> optionalProduct = productRepository.findByCodeAndIsActiveIsTrue(code);
            if (optionalProduct.isPresent()) {
                optionalProduct.get().setIsActive(false);
                productRepository.save(optionalProduct.get());
            }else{
                log.error("Product with code: "+code+" not found!");
                throw new Exception();
            }
        } catch (Exception e) {
            log.error("Error while delete product");
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Page<ProductDto> getProductByCategoryCode(String categoryCode, Pageable paging) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByCodeAndIsActiveIsTrue(categoryCode);
            if (optionalCategory.isPresent()){
                Page<Product> productPage = productRepository.findByCategoriesContainsAndIsActiveIsTrue(optionalCategory.get(),paging);
                return productPage.map(this::getProductDetails);
            }
        }catch (Exception e){
            log.error("Error finding category with code: "+categoryCode);
            e.printStackTrace();
            return Page.empty();
        }
        return null;
    }
    public Page<ProductDto> getProductCodeByCategoryCode(String categoryCode, Pageable pageable) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findByCodeAndIsActiveIsTrue(categoryCode);
            if (optionalCategory.isPresent()){
                List<ProductDto> productPage = productRepository.findByCategoriesContainsAndIsActiveIsTrue(optionalCategory.get()).stream().map(this::getProductDetails).collect(Collectors.toList());
                productPage.sort(Comparator.comparing(ProductDto::getTotalUnitSold,Comparator.reverseOrder()));
                final int start = (int)pageable.getOffset();
                final int end = Math.min((start + pageable.getPageSize()), productPage.size());
                final Page<ProductDto> page = new PageImpl<>(productPage.subList(start, end), pageable, productPage.size());
                return page;
            }

        }catch (Exception e){
            log.error("Error finding category with code: "+categoryCode);
            e.printStackTrace();
        }
        return null;
    }

    private ProductDto getProductDetails(Product product) {
            List<Media> mediaList = mediaService.getMediaByCode(product.getCode());
//            List<ProductInventory> productInventories = productInventoryClient.getProductInventoryByProductCode(product.getCode()).get();
            ProductDto productDto = dtoConverter.productEntityToDto(product);
            productDto.setMediaList(mediaList);
            if (mediaList.size()>0)
                productDto.setImgUrl(mediaList.get(0).getImgUrl());
//            productDto.setInventoryList(productInventories);
            if (product.getReviews().size()>0){
                productDto.setScore((int) product.getReviews().stream().mapToLong(Review::getScore).average().getAsDouble());
            }else {
                productDto.setScore(0);
            }
           productDto.setTotalUnitSold(product.getProductInventories().stream().mapToLong(ProductInventory::getUnitSold).sum());
        productDto.setPrice((long) product.getProductInventories().stream().mapToDouble(ProductInventory::getRetailPrice).min().orElse(0));
        return productDto;
    }
}
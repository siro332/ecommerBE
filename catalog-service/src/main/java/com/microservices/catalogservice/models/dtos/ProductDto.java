package com.microservices.catalogservice.models.dtos;

import com.microservices.catalogservice.models.entities.Media;
import com.microservices.catalogservice.models.entities.product_inventory.Brand;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class ProductDto {
    private String code;
    private String name;
    private String slug;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private String description;
    private Set<CategoryDto> categories;
    private List<Media> mediaList;
    private String imgUrl;
    private long totalUnitSold;
    private long price;
    private Brand brand;
    private Set<ProductInventory> productInventories;
}

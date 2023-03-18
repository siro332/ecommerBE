package com.microservices.catalogservice.models.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class ProductPojo {
    private String code;
    private String name;
    private Boolean isActive;
    private String description;
    private String categoryCode;
    private Integer brandId;
    private Set<ProductInventoryPojo> productInventoryPojos;
    private Integer warrantyPeriod;
}

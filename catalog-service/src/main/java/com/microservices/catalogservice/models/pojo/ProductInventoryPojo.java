package com.microservices.catalogservice.models.pojo;

import com.microservices.catalogservice.models.entities.product_inventory.Discount;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@Data
@NoArgsConstructor
public class ProductInventoryPojo {
    private String sku;
    private Long retailPrice;
    private Long units;
    private Long unitSold;
    private Boolean isActive;
    private String description;
    private HashMap<Integer, String> attributeValues;
}

package com.microservices.catalogservice.models.dtos;

import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private ProductDto productDto;
    private ProductInventory inventoryItem;
    private Integer units;
}

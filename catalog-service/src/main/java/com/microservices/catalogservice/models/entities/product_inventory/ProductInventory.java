package com.microservices.catalogservice.models.entities.product_inventory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microservices.catalogservice.models.entities.Product;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "product_inventories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventory {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String sku;
    @Column(nullable = false)
    //universal product code
    private String productCode;

    @ManyToOne
    @JoinColumn(name = "discount_id")
    @JsonBackReference
    @JsonIgnore
    private Discount discount;
    @ManyToMany
    private Set<ProductAttributeValue> productAttributeValues;
    private Boolean isActive = true;
    private Double retailPrice;
    private Date createdAt;
    private Date updatedAt;
    private Long units = 0L;
    private Long unitSold = 0L;
    @Column(name = "product_id")
    private Long productId;
}
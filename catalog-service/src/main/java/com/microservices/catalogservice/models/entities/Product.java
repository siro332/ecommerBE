package com.microservices.catalogservice.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.microservices.catalogservice.models.entities.product_inventory.Brand;
import com.microservices.catalogservice.models.entities.product_inventory.ProductInventory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    @Nationalized
    private String name;
    private String slug;
    private Boolean isActive;
    @CreatedDate
    private Date createdAt;
    @LastModifiedDate
    private Date updatedAt;
    @Column(columnDefinition = "text")
    private String description;
    @ManyToMany
    private Set<Category> categories;
    @OneToMany
    @JoinColumn(name="product_id")
    @JsonManagedReference
    private Set<ProductInventory> productInventories;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    @JsonBackReference
    @JsonIgnore
    private Brand brand;

}
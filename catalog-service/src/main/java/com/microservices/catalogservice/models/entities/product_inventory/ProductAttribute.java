package com.microservices.catalogservice.models.entities.product_inventory;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_attributes")
public class ProductAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Nationalized
    private String name;
    @Nationalized
    private String description;
}

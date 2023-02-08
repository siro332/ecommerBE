package com.microservices.catalogservice.models.entities.product_inventory;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Nationalized
    private String name;
    @Nationalized
    private String description;
    private Double discountPercent;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}


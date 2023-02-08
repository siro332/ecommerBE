package com.microservices.catalogservice.models.entities.product_inventory;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Nationalized
    private String name;
    private Date createdAt;
}

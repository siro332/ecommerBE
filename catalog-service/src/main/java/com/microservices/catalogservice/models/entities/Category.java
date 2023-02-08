package com.microservices.catalogservice.models.entities;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String code;
    @Column(nullable = false)
    @Nationalized
    private String name;
    private String slug;
    private String parentCode;
    private Boolean isActive = true;
    private Date createdAt;
    private Date updatedAt;
}

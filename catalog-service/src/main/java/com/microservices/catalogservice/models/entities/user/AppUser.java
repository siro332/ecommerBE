package com.microservices.catalogservice.models.entities.user;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "app_user")
public class AppUser {
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
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
}

package com.microservices.catalogservice.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "warranty_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String productSerial;
    private String productCode;
    private Long orderId;
    @Builder.Default
    private Boolean isExpired = false;
}

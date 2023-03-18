package com.microservices.catalogservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarrantyItemDto {
    private Long id;
    private String productSerial;
    private ProductDto productDto;
    private OrderDto orderDto;
    private Boolean isExpired;
    private Date expiredDate;

}

package com.microservices.catalogservice.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private Long id;
    private String code;
    private String name;
    private String address;
    private String zipCode;
    private String phoneNumber;
    private String email;
    private String note;
    private String paymentMethod;
    private boolean isPaid;
    private String userCode;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    private List<CartItemDto> cartItems;
    private Double totals;
}

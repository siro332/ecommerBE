package com.microservices.catalogservice.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Cart {

	private String userCode;
	private List<CartItem> cartItemList;
}











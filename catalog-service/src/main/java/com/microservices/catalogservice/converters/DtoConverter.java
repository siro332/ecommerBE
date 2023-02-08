package com.microservices.catalogservice.converters;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.catalogservice.models.auth.UserDto;
import com.microservices.catalogservice.models.dtos.CartItemDto;
import com.microservices.catalogservice.models.dtos.CategoryDto;
import com.microservices.catalogservice.models.dtos.OrderDto;
import com.microservices.catalogservice.models.dtos.ProductDto;
import com.microservices.catalogservice.models.entities.Category;
import com.microservices.catalogservice.models.entities.Order;
import com.microservices.catalogservice.models.entities.Product;
import com.microservices.catalogservice.models.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DtoConverter {
    private final ModelMapper modelMapper;
    public CategoryDto categoryEntityToDto(Category category){
     return modelMapper.map(category, CategoryDto.class);
    }

    public ProductDto productEntityToDto(Product product){
        return modelMapper.map(product,ProductDto.class);
    }
    public UserDto userEntityToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }
    public OrderDto orderEntityToDto(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            orderDto.setCartItems(mapper.readValue(order.getCartItems(), new TypeReference<List<CartItemDto>>(){}));
        }catch (Exception ignored){
        }
        return modelMapper.map(order,OrderDto.class);
    }
    public Order orderDtoToEntity(OrderDto orderDto){

        Order order = modelMapper.map(orderDto, Order.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            order.setCartItems(mapper.writeValueAsString(orderDto.getCartItems()));
        }catch (Exception ignored){
        }
        return order;
    }

}

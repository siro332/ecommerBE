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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DtoConverter {
    private final ModelMapper modelMapper;
    public CategoryDto categoryEntityToDto(Category category){
     return modelMapper.map(category, CategoryDto.class);
    }

    public ProductDto productEntityToDto(Product product){
        ProductDto productDto = modelMapper.map(product,ProductDto.class);
        productDto.setCategoriesStr(productDto.getCategories().stream().map(CategoryDto::getName).collect(Collectors.joining(", ")));
        return productDto;
    }
    public UserDto userEntityToDto(User user){
        return modelMapper.map(user,UserDto.class);
    }
    public OrderDto orderEntityToDto(Order order){
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<CartItemDto> cartItemDtos = mapper.readValue(order.getCartItems(), new TypeReference<List<CartItemDto>>(){});
            log.info(cartItemDtos.toString());
            orderDto.setCartItems(cartItemDtos);
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderDto;
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
